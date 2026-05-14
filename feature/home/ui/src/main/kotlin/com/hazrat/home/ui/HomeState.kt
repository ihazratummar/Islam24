package com.hazrat.home.ui

import com.hazrat.model.MinimalPrayerData

data class HomeState(
    val dailyQuranDate: String = "",
    val randomAyatNumber: Int = 0,
    val prayerData: MinimalPrayerData = MinimalPrayerData()
)
