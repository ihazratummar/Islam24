# Dependency Optimization & Performance Report

This report analyzes the dependencies across all modules in the **Islam24** project. It identifies unused, redundant, and performance-impacting libraries and suggests better alternatives or native implementations.

## 1. Redundant & Conflicting Libraries

### Gson vs. Kotlinx Serialization
*   **Status:** Conflicting
*   **Usage:**
    *   **Gson:** Used in `core:database`, `feature:alQuran:data`, and `domain:model`.
    *   **Kotlinx Serialization:** Used in `core:remote`, `app` (Navigation), and most feature modules.
*   **Impact:** Having two JSON parsing libraries increases APK size and memory footprint. Gson is slower and lacks the type safety and Kotlin-idiomatic features of Kotlinx Serialization.
*   **Recommendation:** **Consolidate to Kotlinx Serialization.**
    *   Replace `Gson` in `Converter.kt` (Room) and `QuranRepositoryImpl.kt`.
    *   Remove `libs.google-gson` and `libs.converter-gson` from `libs.versions.toml`.

### Accompanist SystemUIController
*   **Status:** Deprecated
*   **Usage:** Declared in `app/build.gradle.kts`.
*   **Impact:** This library is no longer maintained. Modern Android development should use native APIs.
*   **Recommendation:** **Replace with native Edge-to-Edge API.**
    *   Use `enableEdgeToEdge()` in `MainActivity.kt`.
    *   Use `WindowInsetsControllerCompat` for status bar/navigation bar styling.

---

## 2. Performance-Impact Libraries

### Material Icons Extended
*   **Status:** Bad for Performance (Build Time & APK Size)
*   **Usage:** Declared in `app`, `core:ui`, `feature:calendar`, and others.
*   **Impact:** This library contains thousands of icons, significantly increasing build times and adding several MBs to the APK. Analysis shows only about **86 icons** are used across the project.
*   **Recommendation:** **Remove `material-icons-extended-android`.**
    *   Use the standard `Icons` library.
    *   For icons not in the standard library, add the specific SVG/XML files to the `res/drawable` folder or use a filtered dependency.

### AppCompat & Material (View-based) in Library Modules
*   **Status:** Potentially Unnecessary
*   **Usage:** Found in `core:notification`, `feature:allahNames:data`, `domain:repository`, `domain:usecase`, etc.
*   **Impact:** Increases binary size and build time of library modules.
*   **Recommendation:** **Remove from pure Compose and Domain modules.**
    *   Domain modules should ideally be pure Kotlin (`java-library`) to improve build performance.
    *   If a module only uses Compose UI, `appcompat` and the classic `material` (View-based) are not needed.

---

## 3. Unused or Dead Dependencies

### OneSignal
*   **Status:** Unused
*   **Usage:** Root `build.gradle.kts` classpath and `libs.versions.toml`. Not found in any module's `dependencies`.
*   **Impact:** Adds unnecessary bloat to the build script classpath.
*   **Recommendation:** **Remove.**
    *   Remove `onesignal.gradle.plugin` from root `build.gradle.kts`.
    *   Remove `onesignal` entries from `libs.versions.toml`.

### AndroidSVG vs. Coil SVG
*   **Status:** Redundant
*   **Usage:** Both `libs.androidsvg` and `libs.coil-svg` are used.
*   **Impact:** Coil SVG depends on `AndroidSVG`. Declaring both manually in the same module is redundant.
*   **Recommendation:** Remove `libs.androidsvg` and rely on `libs.coil-svg` for SVG rendering in Compose.

---

## 4. Architectural Improvements

### Viewpager2 in App Module
*   **Status:** Redundant
*   **Usage:** Declared in `app/build.gradle.kts`.
*   **Recommendation:** **Use Compose `HorizontalPager` or `VerticalPager`.**
    *   You are already using `HorizontalPager` in `feature:prayertime:ui`. Replace any remaining `Viewpager2` usage with the native Compose Foundation pager.

### Cloudy (Blur Effect)
*   **Status:** Performance Consideration
*   **Usage:** Declared in `app/build.gradle.kts`.
*   **Impact:** Blur effects are computationally expensive.
*   **Recommendation:**
    *   On Android 12 (API 31)+, use the native `Modifier.blur()`.
    *   For older versions, ensure `Cloudy` is only used where absolutely necessary for the UI/UX.

---

## Summary of Action Plan

1.  **Unified Serialization:** Migrate all Gson usage to Kotlinx Serialization.
2.  **Icon Optimization:** Remove `material-icons-extended` and use targeted icons.
3.  **Dependency Cleanup:** Remove OneSignal and redundant AndroidSVG declarations.
4.  **Modernize UI APIs:** Replace Accompanist SystemUIController with native Edge-to-Edge and Viewpager2 with Compose Pagers.
5.  **Lean Library Modules:** Remove `appcompat` and `material` from domain and pure Compose modules.
