# Industry-Grade Islamic App Suggestions

## 1. Calendar Improvements (Premium Feel)
*   **Dual View Toggle:** Instead of completely switching screens, use a "Glassmorphism" overlay for the Hijri date on top of the Gregorian date in a single unified view.
*   **Islamic Events Indicator:** Mark special days (Eid, Ramadan, Ashura) with a **Gold Dot** or a subtle **Spiritual Glow**.
*   **Moon Phase Animation:** Display the actual moon phase of the current Hijri day at the top of the calendar.
*   **Event Bottom Sheet:** On clicking any date, slide up a premium bottom sheet containing:
    *   Full Gregorian & Hijri Date.
    *   Significance of the day (if any).
    *   Sunnah fasts (Mondays/Thursdays or White Days).
    *   Historical events from that Islamic date.

## 2. Typography & Brand Identity
*   **Font Pairing:** Use **Lora** (Serif) for titles to give a spiritual/classic feel, and **Inter** (Sans-Serif) for numbers and body text for high readability.
*   **Arabic Calligraphy:** Integrate actual SVG calligraphy for Surah names and "Bismillah" rather than just standard text.
*   **Color Symbolism:** Use **Emerald Green** (Spirituality), **Spiritual Gold** (Excellence), and **Soft Cream** (Calmness) as your primary palette.

## 3. Engagement Features
*   **Personalized Home Page:** Show the next prayer time prominently with a countdown that changes color (Green -> Yellow -> Red) as time runs out.
*   **Daily Ayah/Hadith Share:** A "Beautiful Card" generator that allows users to share a verse with their name or app branding on social media.
*   **Interactive Prayer Tracker:** A subtle way to track daily prayers with haptic feedback when "checking off" a prayer.
*   **Moon/Sun Sync:** The background of the app should subtly transition from a Dawn/Dusk gradient to a Night theme based on actual Sunrise/Sunset times in the user's location.

## 4. Technical Excellence
*   **Modularization:** Move data access (DAOs, Repositories) into specialized modules like `:core:data:calendar` to keep the `:feature` modules clean.
*   **Offline First:** Ensure all calendar events and basic Islamic data are cached locally for a seamless experience without internet.
*   **Micro-interactions:** Use subtle `HapticFeedback` for calendar selections and Lottie animations for "Empty States".

## 5. Performance & Architectural Audit (Diagnosis)

### 🚨 Why the App Feels "Heavy" & "Sluggish"

1.  **Eager ViewModel Initialization (The "Startup Lag"):**
    *   **Issue:** `MainActivity` initializes **9+ ViewModels** (Quran, Zakat, PrayerTime, Auth, etc.) immediately in `onCreate`.
    *   **Impact:** This consumes significant memory and CPU cycles during startup. Many of these features (like Zakat or Profile) are not needed until the user specifically navigates to them.
    *   **Solution:** Use **Lazy Initialization**. ViewModels should be scoped to their respective Composable screens using `koinViewModel()` or `hiltViewModel()`.

2.  **Anti-Pattern: Passing ViewModels as Parameters:**
    *   **Issue:** `NavGraph` and `AppNavigator` pass a large list of ViewModels down the hierarchy as parameters.
    *   **Impact:** This breaks the principle of "Separation of Concerns." It makes the `NavGraph` extremely heavy, leads to unnecessary recompositions, and prevents Compose from efficiently managing the lifecycle of each screen.
    *   **Solution:** Each screen should "fetch" its own ViewModel from the DI container (Koin). This ensures the ViewModel is only created when the screen is active and destroyed when it leaves the backstack.

3.  **Disabled Navigation Transitions:**
    *   **Issue:** `EnterTransition.None` and `ExitTransition.None` are used globally in `NavHost`.
    *   **Impact:** While this might have been an attempt to hide lag, it actually makes the app feel "stiff" and "unpolished." Smooth transitions (Fade, Slide) provide visual continuity and mask the underlying data loading.
    *   **Solution:** Implement standard Material Design transitions. If the screen stutters during transition, optimize the screen's UI complexity or data loading rather than disabling the animation.

4.  **Dependency Bloat (`material-icons-extended`):**
    *   **Issue:** The app includes `androidx.compose.material:material-icons-extended-android`.
    *   **Impact:** This library contains thousands of icons, significantly increasing the APK size and class-loading time. 
    *   **Solution:** Import only the specific icons used in the project or use SVG/Painter resources.

5.  **Redundant Data Refreshing:**
    *   **Issue:** Some screens (like `QuranScreen`) call `refresh()` in a `LaunchedEffect(Unit)` every time they are visited.
    *   **Impact:** If the ViewModel is already persistent or the data is cached, this causes redundant network/database hits and UI flickering.
    *   **Solution:** Implement a smarter "State Management" where the ViewModel checks if the data is already loaded or "stale" before refreshing.

6.  **"Fat" App Module:**
    *   **Issue:** The `:app` module depends on every single `:feature` and `:core` module.
    *   **Impact:** This results in long build times and a monolithic runtime behavior despite having multiple modules.
    *   **Solution:** Move navigation logic into a dedicated `:navigation` module or use "Feature-by-Contract" to reduce direct dependencies between the app and features.

