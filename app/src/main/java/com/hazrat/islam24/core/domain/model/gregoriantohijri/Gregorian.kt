package com.hazrat.islam24.core.domain.model.gregoriantohijri

data class Gregorian(
    val date: String,
    val day: String,
    val designation: Designation,
    val format: String,
    val month: Month,
    val weekday: Weekday,
    val year: String
)