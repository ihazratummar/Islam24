package com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel

data class Gregorian(
    val date: String,
    val day: String,
    val designation: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Designation,
    val format: String,
    val month: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Month,
    val weekday: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Weekday,
    val year: String
)