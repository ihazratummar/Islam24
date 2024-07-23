package com.hazrat.islam24.core.presentation.prayertime.setting

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface PrayerSettingEvent {

    data object OpenCalculationDialog : PrayerSettingEvent
    data object OpenJuristicDialog : PrayerSettingEvent

    data class CalculationChanged(val value: Int) : PrayerSettingEvent
    data class JuristicChanged(val value: Int) : PrayerSettingEvent


}