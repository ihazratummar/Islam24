package com.hazrat.islam24.core.presentation.prayertime

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface PrayerEvent {
    data object SharePrayer: PrayerEvent
    data object RefreshPrayer: PrayerEvent
}