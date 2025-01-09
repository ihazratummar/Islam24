package com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models

data class Hijri(
    val date: String,
    val day: Any,
    val designation: Designation,
    val format: String,
    val holidays: List<String?>,
    val month: MonthHijri,
    val weekday: WeekdayHijri,
    val year: Int
)