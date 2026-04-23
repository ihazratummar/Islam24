package com.hazrat.prayer.ui

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface PrayerEvent {
    data object SharePrayer: PrayerEvent
    data object RefreshPrayer: PrayerEvent
}