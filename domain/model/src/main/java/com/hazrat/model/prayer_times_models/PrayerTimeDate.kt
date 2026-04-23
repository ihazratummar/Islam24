package com.hazrat.model.prayer_times_models

data class PrayerTimeDate(
    val gregorian: com.hazrat.model.prayer_times_models.Gregorian,
    val hijri: com.hazrat.model.prayer_times_models.Hijri,
    val readable: String,
    val timestamp: String
)