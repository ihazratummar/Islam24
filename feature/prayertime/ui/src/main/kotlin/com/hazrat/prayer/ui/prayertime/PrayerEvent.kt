package com.hazrat.prayer.ui.prayertime

import com.hazrat.model.Prayer
import java.time.LocalDate

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface PrayerEvent {
    data object SharePrayer : PrayerEvent
    data object RefreshPrayer : PrayerEvent

    data class LogPrayer(val date: Long, val  prayer: Prayer) : PrayerEvent
}

sealed class PrayerTimeUiEvent {
    data class ShowError(val message: String) : PrayerTimeUiEvent()

}