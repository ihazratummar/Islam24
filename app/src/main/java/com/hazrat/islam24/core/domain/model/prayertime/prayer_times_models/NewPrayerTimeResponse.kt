package com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models

data class NewPrayerTimeResponse(
    val code: Int,
    val `data`: Map<String, List<DailyData>>,
    val status: String
)