package com.hazrat.prayer.ui.setting

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface PrayerSettingEvent {

    data class CalculationChanged(val value: Int) : PrayerSettingEvent
    data class JuristicChanged(val value: Int) : PrayerSettingEvent


}