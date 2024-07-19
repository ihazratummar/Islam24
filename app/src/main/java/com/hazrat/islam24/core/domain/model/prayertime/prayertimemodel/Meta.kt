package com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel

data class Meta(
    val latitude: Double,
    val latitudeAdjustmentMethod: String,
    val longitude: Double,
    val method: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Method,
    val midnightMode: String,
    val offset: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Offset,
    val school: String,
    val timezone: String
)