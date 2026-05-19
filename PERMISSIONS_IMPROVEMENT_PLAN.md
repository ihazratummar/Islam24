# Permissions Improvement Plan: Industry Standards & Best Practices

This document outlines the strategy to refactor and optimize runtime permissions across the Islam24 application, focusing on the `feature:prayertime` module and global architectural consistency.

## 1. Current Issues Identified
- **Inefficiency:** `rememberPermissionRequester` is called inside `itemsIndexed` in `PrayerTimeScreen.kt`. This recreates the requester for every list item, which is a performance bottleneck and bad practice.
- **Inconsistency:** Location permission logic is fragmented. `HomeScreen` shows an address but `PrayerTimeScreen` handles the refresh request independently.
- **Lack of Transparency:** No clear "Rationale" UI before asking for sensitive permissions (Location).
- **Hard-to-Maintain:** Permission logic is mixed directly into the UI components (Scaffold/LazyColumn), making it hard to test or reuse.
- **Missing State Synchronization:** If a user grants permission in settings, the app doesn't always react immediately without a manual refresh.

---

## 2. Proposed Architecture: The "Permission Gate" Strategy

Instead of scattered "request" calls, we will move to a **State-Driven** permission system.

### A. Centralized Permission Manager (`core:permission`)
Enhance the existing `PermissionManager` to handle:
- **Permission State Tracking:** Using a Flow/State to observe permission status.
- **Rationale Handling:** A standardized way to show *why* we need the permission before the system dialog appears.

### B. The `PermissionGate` Component
A reusable high-order component that wraps screens or features:
```kotlin
@Composable
fun PermissionGate(
    permission: String,
    rationaleTitle: String,
    rationaleMessage: String,
    content: @Composable () -> Unit
) {
    // 1. Check current status
    // 2. If granted -> Show content
    // 3. If denied -> Show Rationale UI with a "Grant" button
    // 4. If permanently denied -> Show "Go to Settings" UI
}
```

---

## 3. Clear Implementation Path

### Step 1: Fix `PrayerTimeScreen.kt` Duplication
- **Move Requester to Top:** Pull `requestNotificationPermission` out of the `itemsIndexed` block. A single requester can handle all notification toggles by passing different logic to the `onGranted` lambda or handling it in the `onNotificationClick` callback.
- **Unified Location Logic:** Ensure `PrayerEvent.RefreshPrayer` handles the permission check internally or via a central `PermissionViewModel`.

### Step 2: Implement "Rationale First" (Industry Standard)
Never trigger the system dialog "cold" for sensitive data like Location.
1. Show a beautiful custom Card/Dialog explaining: "To show accurate prayer times for your current city, we need location access."
2. User clicks "Allow".
3. *Then* trigger the `rememberPermissionRequester`.

### Step 3: Global Permission Lifecycle
- **`onStart` Check:** When the app returns from background/settings, re-verify permissions.
- **DataStore Integration:** Save "Rationale Shown" state to avoid pestering users who have permanently denied.

---

## 4. Specific Action Items for `feature:prayertime`

1.  **Refactor `PrayerTimeScreen`:**
    - Remove `rememberPermissionRequester` from the loop.
    - Create a single `notificationPermissionLauncher` at the top level of the screen.
2.  **Location Refresh:**
    - Use a single `locationPermissionLauncher` at the top.
    - Combine `PullToRefresh` logic with a permission check that triggers the rationale if missing.
3.  **Industry Grade UI:**
    - Create a `LocationPermissionRequiredPlaceholder` for when the user denies location but tries to view prayer times. It should have a "Grant Permission" button.

---

## 5. Summary of Duplicate/Inefficient Points
- **File:** `PrayerTimeScreen.kt`
    - **Line 268:** `rememberPermissionRequester` inside `itemsIndexed`. **CRITICAL FIX**.
    - **Line 108:** `PullToRefresh` permission logic is manual. Should be handled by a delegate.
    - **Inconsistency:** Some parts use `isPermissionGranted` (utility) and some use `rememberPermissionRequester` (composable). These should be synchronized.

---
*Prepared by Gemini CLI*
