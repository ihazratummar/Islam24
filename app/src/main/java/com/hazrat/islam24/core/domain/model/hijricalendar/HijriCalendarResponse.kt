package com.hazrat.islam24.core.domain.model.hijricalendar

import com.hazrat.islam24.core.domain.model.hijricalendar.Data

data class HijriCalendarResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)