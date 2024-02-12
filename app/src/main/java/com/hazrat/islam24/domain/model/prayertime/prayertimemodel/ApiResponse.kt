package com.hazrat.islam24.domain.model.prayertime.prayertimemodel

data class ApiResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)