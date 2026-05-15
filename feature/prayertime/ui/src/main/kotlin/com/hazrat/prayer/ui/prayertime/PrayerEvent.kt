package com.hazrat.prayer.ui.prayertime

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface PrayerEvent {
    data object SharePrayer : PrayerEvent
    data object RefreshPrayer : PrayerEvent
}

sealed class PrayerTimeUiEvent {
    data class ShowError(val message: String) : PrayerTimeUiEvent()
}