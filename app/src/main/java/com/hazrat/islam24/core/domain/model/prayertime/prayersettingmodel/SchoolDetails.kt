package com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel

data class SchoolDetails(
    val number: Int,
    val name: String
)

val schoolDetailsList = listOf(
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.SchoolDetails(
        number = 0,
        name = "Shafi'i, Maliki & Hanbali"
    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.SchoolDetails(
        number = 1,
        name = "Hanafi"
    )
)
