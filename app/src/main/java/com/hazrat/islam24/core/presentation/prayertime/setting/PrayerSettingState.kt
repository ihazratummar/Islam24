package com.hazrat.islam24.core.presentation.prayertime.setting

data class PrayerSettingState(
    val calculationMethod: Int = 1,
    val juristic: Int = 0,
    val isCalculationDialogOpen: Boolean = false,
    val isJuristicDialogOpen: Boolean = false,
    val isRefresh: Boolean = false
)
