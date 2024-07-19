package com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel

data class Hijri(
    val date: String,
    val day: String,
    val designation: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Designation,
    val format: String,
    val holidays: List<String>,
    val month: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.MonthX,
    val weekday: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.WeekdayX,
    val year: String
)