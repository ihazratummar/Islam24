package com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel

data class Date(
    val gregorian: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Gregorian,
    val hijri: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Hijri,
    val readable: String,
    val timestamp: String
)