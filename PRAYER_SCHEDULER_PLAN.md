# Industry-Grade Prayer Scheduler Plan

This document outlines the strategic plan to upgrade the current prayer scheduling system to an industry-grade, reliable, and battery-efficient architecture.

## 1. Current Architecture Analysis
- **Mechanism:** Uses `AlarmManager.setExactAndAllowWhileIdle` for precision.
- **Handling:** Triggered via `PrayerTimeReceiver` (BroadcastReceiver).
- **Persistence:** Reschedules alarms on boot via `BootReceiver`.
- **Playback:** Azan is played directly in the `BroadcastReceiver` context using `MediaPlayerHelper`.
- **Configuration:** Supports Silent, Default, and Azan notification types.

### Identified Risks & Limitations
1. **Receiver Timeout:** `BroadcastReceiver.onReceive` has a limited execution time. Long Azans might be cut off if the system kills the receiver process.
2. **Missing Rescheduling Triggers:** Alarms are not rescheduled on timezone changes, time changes, or app updates.
3. **Database Dependency:** If the database isn't populated for the current day, alarms won't be set.
4. **Exact Alarm Restrictions:** Modern Android (13+) requires specific user permission for exact alarms, which can be revoked at any time.
5. **OEM Background Restrictions:** Aggressive battery management on devices (Samsung, Xiaomi, etc.) can kill the app's ability to fire alarms.
6. **Thread Safety:** `runBlocking` in `onReceive` can cause ANRs if database access is slow.

---

## 2. Proposed Industry-Level Architecture

### A. Reliability: The "Triple-Lock" System
To ensure prayer times are NEVER missed, we will implement three layers of scheduling:

1.  **AlarmManager (Primary):** Precise timing for notifications and Azan.
2.  **WorkManager (Secondary):** A "Janitor" job that runs periodically (e.g., every 6-12 hours) to ensure alarms for the next 24 hours are correctly scheduled in the `AlarmManager`.
3.  **Foreground Service (Playback):** When an Azan alarm fires, it starts a `ForegroundService` to handle audio playback. This prevents the system from killing the audio mid-stream.

### B. Comprehensive Rescheduling
Register for the following system broadcasts to ensure alarms are always up-to-date:
- `ACTION_BOOT_COMPLETED` (Already implemented)
- `ACTION_TIMEZONE_CHANGED`: Critical for users traveling.
- `ACTION_TIME_CHANGED`: When the user manually adjusts system time.
- `ACTION_MY_PACKAGE_REPLACED`: After an app update, alarms are often cleared.
- `ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED`: Re-schedule if the user grants the permission.

### C. Azan Playback via Foreground Service
- **Why:** Notifications with long audio (Azan) are best handled by a service with a persistent notification.
- **Benefit:** Allows the user to "Mute" or "Stop" the Azan from the notification bar reliably.
- **Implementation:** `PrayerTimeReceiver` will start `AzanPlaybackService` which manages `MediaPlayer` and shows a "Playing Azan..." notification.

---

## 3. Battery Optimization & OEM Compatibility

Industry-grade apps must navigate the "Don't Kill My App" landscape:
1.  **Battery Optimization Guide:** Provide a UI flow to ask users to exclude the app from battery optimization.
2.  **Inexact Alarms fallback:** If `SCHEDULE_EXACT_ALARM` is not granted, fall back to `setAndAllowWhileIdle` (inexact) rather than failing completely.
3.  **Power Manager checks:** Verify `isIgnoringBatteryOptimizations()` and notify the user if they are at risk of missing prayer times.

---

## 4. Technical Implementation Suggestions

### Code Structure Improvements
- **`PrayerScheduler` Interface:** Decouple the scheduling logic from Android components.
- **Use `goAsync()` consistently:** Avoid `runBlocking` in all Receivers.
- **State Management:** Track "Next Scheduled Alarm" in DataStore to avoid redundant scheduling and for debugging.

### Data Integrity
- **Prefetching:** Ensure prayer times for the next 7 days are always cached.
- **Fallback Logic:** If today's data is missing, attempt an emergency background sync via `WorkManager`.

---

## 5. Roadmap for Implementation

| Phase | Task | Description |
| :--- | :--- | :--- |
| **Phase 1** | **Rescheduling Logic** | Add Timezone, Time Change, and Update receivers. |
| **Phase 2** | **Azan Service** | Migrate Azan playback from `MediaPlayerHelper` to `AzanPlaybackService`. |
| **Phase 3** | **WorkManager Backup** | Implement the "Janitor" worker to verify and fix schedules. |
| **Phase 4** | **Power Management** | Add UI for battery optimization exclusion and permission handling. |
| **Phase 5** | **Logging & Monitoring** | Add internal logging for "Last Alarm Fired" and "Next Alarm Scheduled". |

---

## 6. Industry Best Practices Check-list
- [ ] Uses `PendingIntent.FLAG_IMMUTABLE`.
- [ ] Handles Android 13+ Notification Permissions.
- [ ] Handles Android 12+ Exact Alarm Permissions.
- [ ] Implements `DirectBootAware` for alarms immediately after boot (before decryption).
- [ ] Provides a "Test Alarm" feature in settings for user peace of mind.
- [ ] Uses `AudioAttributes.USAGE_ALARM` for Azan to respect alarm volume settings.

---
*Prepared by Gemini CLI for Hazrat Ummar Shaikh*
