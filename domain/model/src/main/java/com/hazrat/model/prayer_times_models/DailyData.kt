package com.hazrat.model.prayer_times_models

data class DailyData(
    val date: com.hazrat.model.prayer_times_models.PrayerTimeDate,
    val meta: com.hazrat.model.prayer_times_models.PrayerTimeMetaData,
    val timings: com.hazrat.model.prayer_times_models.Timings
)