package com.hazrat.model.prayer_times_models

data class PrayerTimeMetaData(
    val latitude: Double,
    val latitudeAdjustmentMethod: String,
    val longitude: Double,
    val method: com.hazrat.model.prayer_times_models.PrayerMethods,
    val midnightMode: String,
    val offset: com.hazrat.model.prayer_times_models.Offset,
    val school: String,
    val timezone: String
)