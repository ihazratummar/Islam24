package com.hazrat.model.prayer_times_models

data class PrayerMethods(
    val id: Int,
    val location: com.hazrat.model.prayer_times_models.PrayerLocation,
    val name: String,
    val params: com.hazrat.model.prayer_times_models.PrayerParams
)