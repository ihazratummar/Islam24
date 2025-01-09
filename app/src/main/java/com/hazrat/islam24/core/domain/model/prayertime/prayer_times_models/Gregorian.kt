package com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models

data class Gregorian(
    val date: String,
    val day: String,
    val designation: Designation,
    val format: String,
    val month: MonthGregorian,
    val weekday: Weekday,
    val year: String
)