package com.hazrat.islam24.domain.model.prayertime.prayertimemodel

data class Date(
    val gregorian: Gregorian,
    val hijri: Hijri,
    val readable: String,
    val timestamp: String
)