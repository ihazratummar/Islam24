# Islam24 Technical Excellence & Industry Standard Roadmap

This document serves as the master plan for transforming Islam24 into a top-tier, industry-grade application. It covers completed architectural upgrades and planned optimizations for permissions and reliability.

---

## 1. Reliable Prayer Scheduling (The "Triple-Lock" System)
*Status: Implemented & Integrated*

To ensure prayer times are never missed despite Android's aggressive battery management, we have implemented a three-layer redundancy system.

### A. Architectural Components
1.  **Primary: AlarmManager (Precision)**
    - Uses `setExactAndAllowWhileIdle` for to-the-minute accuracy.
    - Fallback logic for devices where "Exact Alarm" permission is denied.
2.  **Secondary: WorkManager (Self-Healing)**
    - `PrayerJanitorWorker` runs every 12 hours.
    - It verifies that the next 24 hours of alarms are correctly scheduled in the system.
3.  **Tertiary: Foreground Service (Reliable Playback)**
    - `AzanPlaybackService` handles long-running audio (Azan).
    - Prevents the system from killing the audio mid-stream, which happens frequently in standard `BroadcastReceivers`.

### B. Multi-Module Alignment
- **`core:notification`**: Contains the logic for scheduling, workers, and services.
- **`app`**: Handles the manifest registrations and global startup scheduling.

---

## 2. Advanced Permission Management
*Status: Research Completed / Ready for Implementation*

The goal is to move from "scattered requests" to a "State-Driven Permission Gate" architecture.

### A. Key Optimization: The "Permission Gate"
Instead of checking permissions inside every UI component, we wrap features in a "Gate" that handles the state:
- **State 1: Granted** -> Show the feature immediately.
- **State 2: Denied (Show Rationale)** -> Show a custom, beautiful UI explaining *why* the permission is needed (Location for prayer times, Notifications for Azan).
- **State 3: Permanently Denied** -> Show a "Go to Settings" prompt with clear instructions.

### B. Specific Fixes for `feature:prayertime`
- **Stop Launcher Duplication:** Move `rememberPermissionRequester` out of `LazyColumn` loops. Re-use a single launcher at the screen's top level.
- **Unified Location Logic:** Synchronize `HomeScreen` location data with `PrayerTimeScreen` to avoid redundant requests.

---

## 3. Multi-Module Engineering Standards

To maintain a clean and scalable codebase, all future "Industry Level" changes must follow these rules:

1.  **Boundary Respect:** 
    - UI modules (`feature:*`) should only trigger permission requests.
    - Logic for checking/managing status lives in `core:permission`.
2.  **Thread Safety:** 
    - All `BroadcastReceivers` MUST use `goAsync()` when accessing the database or DataStore.
    - Use `runBlocking(Dispatchers.IO)` sparingly and only when the system context requires it.
3.  **Dependency Injection (Koin):**
    - Every new service or worker must be registered in its respective module's Koin file.
    - Use `worker { ... }` for WorkManager classes.

---

## 4. Implementation Roadmap

| Phase | Focus | Key Task |
| :--- | :--- | :--- |
| **Phase 1** | **Scheduler** | Triple-Lock Integration (Completed) |
| **Phase 2** | **Permissions** | Refactor `PrayerTimeScreen` launchers (Next) |
| **Phase 3** | **Permissions** | Implement Global `PermissionGate` UI components |
| **Phase 4** | **Reliability** | Add Battery Optimization Exclusion UI |
| **Phase 5** | **Cleanup** | Consolidate duplicate location logic across Home/Prayer modules |

---
*Prepared by Gemini CLI for Hazrat Ummar Shaikh*
