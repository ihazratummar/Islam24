package com.hazrat.model.prayer_times_models

data class Hijri(
    val date: String,
    val day: Any,
    val designation: com.hazrat.model.prayer_times_models.Designation,
    val format: String,
    val holidays: List<String?>,
    val month: com.hazrat.model.prayer_times_models.MonthHijri,
    val weekday: com.hazrat.model.prayer_times_models.WeekdayHijri,
    val year: Int
)