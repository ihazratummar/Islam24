package com.hazrat.islam24.core.domain.model.hijricalendar

data class HijriCalendarResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)