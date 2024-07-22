package com.hazrat.islam24.core.presentation.prayertime.setting

import com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.CalculationMethodDetails

data class PrayerSettingState(
    val calculationMethod: Int = 1,
    val juristic: Int = 0,
    val isCalculationDialogOpen: Boolean = false,
    val isJuristicDialogOpen: Boolean = false
)
