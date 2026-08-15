## [4.3.0] - 2026-08-15
- **Hisnul Muslim & Dua Hub**: 11 curated Dua categories with authentic citations and custom Islamic banners.
- **Dua Share Card Creator**: Export and share branded gradient Dua cards directly to WhatsApp, Instagram & Facebook Stories.
- **Interactive Dua Actions**: Tap-anchored context menu with Bookmark, Copy, and Share options.
- **Royal Digital Tasbih**: 13 authentic Sunnah Dhikr presets with a 33-bead luminous radial dial and tactile tap haptics.
- **Official Arabic Calligraphy**: Applied crystal-clear Scheherazade Uthmani typography across Duas and Tasbih.
- **Reliability & Performance**: Fixed recent items persistence across app restarts, enhanced prayer settings, and refined theme contrast.

## [4.2.0] - 2026-08-09
- **Support Islam 24**: Support the app with voluntary Sadaqah in your local currency. 100% free with zero ads forever!
- **Live Supporter Ticker**: See real-time community support updates right on the screen.
- **Khatam Quran Tracker**: Easily set goals (7 to 180 days) and track your full Quran reading journey.
- **Faster Account Switch**: Log in or switch accounts smoothly without restarting the app.
- **Performance & Fixes**: Smoother screens, connection improvements, and overall bug fixes.


## [4.1.0] - 2026-07-28

### What's New
- **Khatam Quran (Reading Plan)**: Brand-new Khatam Quran tracking feature replacing the progress tab. Set your target completion date (7 to 180 days) and track your full Quran reading journey with real-time percentage progress.
- **Emerald Khatam Dashboard**: Beautiful hero card featuring an emerald gradient theme, live progress bar, current reading position pill (*Surah Al-Baqarah: 89*), remaining days countdown, and former plans history.
- **Dedicated Continuation**: Tap *"Continue Reading"* on the Khatam tab to jump straight to your exact reading position.
- **Independent Reading Progress**: Khatam reading and regular Read tab recents are 100% isolated into separate data streams, ensuring regular reading doesn't affect your Khatam plan and vice versa.

### Improvements & Fixes
- **Resume Position Protection**: Your furthest unread position in Khatam is protected — accidentally swiping back to review past Surahs will never regress your Khatam resume point.
- **Dwell-Time Scroll Tracking**: Position saving is debounced by 600ms so fast flings or peeking ahead won't overwrite your last read Ayah position.
- **Khatam Paging Restriction**: Right-swiping to advance to the next Surah in Khatam mode is locked until reaching the final Ayah of the current Surah.
- **Prayer Time Calculations**: Fixed timezone string parsing in prayer calculation algorithms to prevent zero-duration countdown timer issues.
- **Clean Dependency Tree**: Removed unused Firebase (Auth/Storage) and Google Maps libraries to optimize build size and APK overhead.

## [4.0.1] - 2026-07-28

### What's New
- **Ayah Bookmarks**: Bookmark any Ayah while reading and access all your bookmarks from a dedicated tab in the Surah screen, organized by Surah name.
- **Surah Calligraphy in Top Bar**: Beautiful Arabic calligraphy of the current Surah name is now displayed in the Ayah screen's top bar.

### Improvements & Fixes
- **Recent Read Tracking**: Fixed a critical issue where the app was only recording the first Ayah instead of tracking scroll position. Your reading progress now accurately updates as you scroll.
- **Surah Completion**: Completing a Surah (reaching the last Ayah) now correctly removes it from recent reads.
- **Swipe Navigation**: Swiping to the next Surah from the Ayah screen now correctly records the new Surah in recent reads.
- **Play Console Compliance**: Resolved restricted foreground service warning for Android 15+ by migrating Zakat alarm scheduling to use inexact alarms.
- **Edge-to-Edge Display**: Removed deprecated system bar color APIs for full Android 15 edge-to-edge compatibility.
- **Performance**: Stripped debug logging from release builds and improved R8 optimization configuration for a smaller, faster APK.
- **Audio Playback**: Fine-tuned leading and trailing silence detection for smoother Ayah-by-Ayah audio playback.

## [4.0.0-Beta 4] - 2026-07-26

### What's New
- **Support Islam 24**: You can now support the app through voluntary Sadaqah gifts! Islam 24 remains 100% free with zero ads, zero tracking, and zero paywalls for the entire Ummah — forever.
- **Regional Currency Support**: Support prices and totals now automatically display in your country's local currency (₹, $, €, SAR, etc.).
- **Vibrant Home Screen Redesign**: Refreshed Home screen icons with vivid, beautiful colors that look stunning in both Light Mode and Dark Mode.
- **Offline Connection Banner**: A friendly alert lets you know whenever your internet connection is turned off.
- **Easy Support Access**: Quick access to support the app directly from the Home screen and the end of the 99 Names of Allah screen.

### Improvements & Fixes
- Replaced icons across the app with crisp, high-resolution vector assets for a cleaner look.
- Polished support cards and layout badges for smooth viewing on all screen sizes.
- General performance improvements and bug fixes for a smoother experience.

## [3.1.1] - 2025-03-14
### Fixed
- Fixed a crash when toggling prayer notifications on devices running Android versions below API 33.

## [3.1.0] - 2025-03-14
### Added
- Brand new Profile screen with a cleaner, more modern look.
- Support for dynamic titles on Policy and Legal pages.
- Cleaner reading experience on Policy pages by removing website headers and footers.
- Added Dua section for daily supplications.
- Full support for Android 15.

### Changed
- Manual sign-in has been temporarily removed. Google Sign-in will be returning in the next update!
- Completely redesigned Home screen and fresh app colors.
- Improved Qibla direction accuracy.

### Fixed
- Fixed a major crash that prevented some users from accessing their profile.
- Fixed an issue where some app settings were not appearing correctly.
- Improved overall app stability and performance.
- Fixed notification issues on some devices.

## [3.0.0] - 2024-05-22
### Added
- Initial release of the new redesign.
- Enhanced prayer times calculation for high latitudes.
- New 'What's New' feature to keep you updated.

### Fixed
- General bug fixes and performance improvements.
