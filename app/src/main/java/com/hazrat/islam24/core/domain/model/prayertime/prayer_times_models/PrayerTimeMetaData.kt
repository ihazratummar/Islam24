package com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models

data class PrayerTimeMetaData(
    val latitude: Double,
    val latitudeAdjustmentMethod: String,
    val longitude: Double,
    val method: PrayerMethods,
    val midnightMode: String,
    val offset: Offset,
    val school: String,
    val timezone: String
)