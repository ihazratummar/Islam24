package com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models

data class PrayerMethods(
    val id: Int,
    val location: PrayerLocation,
    val name: String,
    val params: PrayerParams
)