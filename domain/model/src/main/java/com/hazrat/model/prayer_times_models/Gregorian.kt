package com.hazrat.model.prayer_times_models

data class Gregorian(
    val date: String,
    val day: String,
    val designation: com.hazrat.model.prayer_times_models.Designation,
    val format: String,
    val month: com.hazrat.model.prayer_times_models.MonthGregorian,
    val weekday: com.hazrat.model.prayer_times_models.Weekday,
    val year: String
)