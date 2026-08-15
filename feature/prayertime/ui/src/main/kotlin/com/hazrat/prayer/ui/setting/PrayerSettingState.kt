package com.hazrat.prayer.ui.setting

import com.hazrat.model.prayersettingmodel.UserPrayerSettingModel

data class PrayerSettingState(
    val isCalculationDialogOpen: Boolean = false,
    val isJuristicDialogOpen: Boolean = false,
    val isRefresh: Boolean = false,
    val userPrayerSettingModel: UserPrayerSettingModel? = null
)
