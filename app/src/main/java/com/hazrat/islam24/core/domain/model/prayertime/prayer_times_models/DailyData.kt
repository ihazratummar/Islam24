package com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models

data class DailyData(
    val date: PrayerTimeDate,
    val meta: PrayerTimeMetaData,
    val timings: Timings
)