package com.hazrat.home.ui

import com.hazrat.model.IslamicEventsInfoModel
import com.hazrat.model.MinimalPrayerData
import com.hazrat.usecase.UpcomingIslamicEvent

data class HomeState(
    val dailyQuranDate: String = "",
    val randomAyatNumber: Int = 0,
    val prayerData: MinimalPrayerData = MinimalPrayerData(),
    val upcomingIslamicEvent: UpcomingIslamicEvent? = null,
    val islamicEventsInfoModel: List<IslamicEventsInfoModel?> = emptyList(),
    val fridayTime: Long? = null,
    val isLocationLoading: Boolean = false,
    val isPrayerTimeLoading: Boolean = false
)
