package com.hazrat.prayer.ui.setting

data class PrayerSettingState(
    val calculationMethod: Int = 1,
    val juristic: Int = 0,
    val isCalculationDialogOpen: Boolean = false,
    val isJuristicDialogOpen: Boolean = false,
    val isRefresh: Boolean = false
)
