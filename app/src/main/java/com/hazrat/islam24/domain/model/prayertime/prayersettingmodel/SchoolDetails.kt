package com.hazrat.islam24.domain.model.prayertime.prayersettingmodel

data class SchoolDetails(
    val number: Int,
    val name: String
)

val schoolDetailsList = listOf(
    SchoolDetails(
        number = 0,
        name = "Shafi'i, Maliki & Hanbali"
    ),
    SchoolDetails(
        number = 1,
        name = "Hanafi"
    )
)
