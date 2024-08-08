package com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel

data class JuristicMethodDetails(
    val number: Int,
    val name: String
)

val schoolDetailsList = listOf(
    JuristicMethodDetails(
        number = 0,
        name = "Shafi'i, Maliki & Hanbali"
    ),
    JuristicMethodDetails(
        number = 1,
        name = "Hanafi"
    )
)
