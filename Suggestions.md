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

