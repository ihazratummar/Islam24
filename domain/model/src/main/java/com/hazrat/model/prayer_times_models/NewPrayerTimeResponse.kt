package com.hazrat.model.prayer_times_models

data class NewPrayerTimeResponse(
    val code: Int,
    val `data`: Map<String, List<com.hazrat.model.prayer_times_models.DailyData>>,
    val status: String
)