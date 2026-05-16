package com.hazrat.database.entity

data class HolidayInfoEntity(
    val holidays: List<String>,
    val gregorianDate: String,
    val hijriDate: String
)