package com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel

data class ApiResponse(
    val code: Int,
    val `data`: List<com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Data>,
    val status: String
)