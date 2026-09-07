## [4.4.1] - 2026-09-04
- **Security Compliance (Google Play Policy)**: Removed unsafe WebView SSL error bypass (`SslErrorHandler.proceed()`) to strictly adhere to Google Play Device and Network Abuse security policy and ensure secure TLS/SSL network communication.

## [4.4.0] - 2026-08-31

### Compact Summary
- **Multi-Translation Engine**: Switch instantly between Maulana Muhiuddin Khan, Taisirul Quran, Dr. Mujibur Rahman, and Mufti Taqi Usmani (English).
- **Bengali Pronunciation (বাংলা উচ্চারণ)**: Full 6,236 Ayah Bengali transliteration preloaded offline.
- **Deep Bengali Localization**: 114 Surah names/meanings, Bengali numerals, and localized relative dates.
- **Auth Token Rotation**: Seamless auto-refresh and persistent session protection.

## [4.3.3] - 2026-08-25
- **Launcher Widget Previews**: Added full layout previews (`android:previewLayout`) for Next Prayer, Hijri Date, and Hijri Calendar widgets in the Android system widget picker.
- **Glance Layout Fix**: Resolved Glance 10-element constraint (`Column container cannot have more than 10 elements`) in `NextPrayerWidget` by restructuring into balanced sub-containers.
- **Prayer Notification Accuracy**: Fixed double notification triggers (pre-alert vs prayer time) by ensuring exact `AlarmManager` Intent cancellation.
- **Instant Login Reschedule**: Restoring prayer settings/offsets on login or cloud sync now automatically cancels stale device alarms and reschedules prayer notifications immediately.
- **Battery Optimization**: Removed unused widget receivers and optimized background WorkManager jobs to guarantee zero idle battery drain.

## [4.3.2] - 2026-08-16
- **FIX Quran & Khatam Cloud Sync**: Fixed Quran, Prayer Logs, User Setting Backup sync

## [4.3.1] - 2026-08-16
- **Quran & Khatam Cloud Sync**: Full bidirectional sync across devices for Khatam reading plans, Ayah bookmarks, recent reads, prayer logs, and settings with conflict resolution.
- **Harmonized Juz View**: Redesigned Juz reading mode with official Arabic SVG calligraphy vectors and Ayah range badges matching Surah cards.
- **Dedicated Quran Bookmarks Engine**: Separated bookmark data into dedicated syncable storage with real-time reactive joins, keeping static Quran text pure and fast.
- **Social Sharing Resilience**: Added Android 11+ package visibility and intelligent fallback to system share sheet for WhatsApp, Instagram Stories, and Facebook.
- **Performance & Cleanup**: Hard delete cleanup for synchronized removals, fixed soft-delete queries, and optimized Room query mappings.

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
