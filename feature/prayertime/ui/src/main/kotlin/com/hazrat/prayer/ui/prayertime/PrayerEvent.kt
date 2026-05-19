package com.hazrat.prayer.ui.prayertime


import com.hazrat.model.Prayer

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface PrayerEvent {
    data object SharePrayer : PrayerEvent
    data object RefreshPrayer : PrayerEvent

    data class LogPrayer(val date: Long, val  prayer: Prayer) : PrayerEvent

    data class PrayerNotificationToggle(val prayer: Prayer, val enabled: Boolean, val prayerTIme: Long = 0L) : PrayerEvent
}

sealed class PrayerTimeUiEvent {
    data class ShowError(val message: String) : PrayerTimeUiEvent()

}
