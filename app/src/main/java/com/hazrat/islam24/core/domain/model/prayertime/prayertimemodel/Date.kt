package com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel

data class Date(
    val gregorian: Gregorian,
    val hijri: Hijri,
    val readable: String,
    val timestamp: String
)