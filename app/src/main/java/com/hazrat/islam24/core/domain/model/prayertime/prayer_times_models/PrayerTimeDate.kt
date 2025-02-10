package com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models

data class PrayerTimeDate(
    val gregorian: Gregorian,
    val hijri: Hijri,
    val readable: String,
    val timestamp: String
)