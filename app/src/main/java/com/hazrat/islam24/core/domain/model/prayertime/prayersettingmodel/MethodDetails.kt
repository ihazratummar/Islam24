package com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel

data class MethodDetails(
    val method: Int,
    val name: String
)

val prayerMethods = listOf(
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 0,
        name = "Shia Ithna-Ashari"
    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 1,
        name = "University of Islamic Sciences, Karachi"
    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 2,
        name = "Islamic Society of North America"
    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 3,
        name = "Muslim World League"
    ),
//    MethodDetails(
//        method = 4,
//        name = "Umm Al-Qura University, Makkah"
//    ),
//    MethodDetails(
//        method = 5,
//        name = "Egyptian General Authority of Survey"
//    ),
//    MethodDetails(
//        method = 7,
//        name = "Institute of Geophysics, University of Tehran"
//    ),
//    MethodDetails(
//        method = 8,
//        name = "Gulf Region"
//    ),
//    MethodDetails(
//        method = 9,
//        name = "Kuwait"
//    ),
//    MethodDetails(
//        method = 10,
//        name = "Qatar"
//    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 11,
        name = "Majlis Ugama Islam Singapura, Singapore"
    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 12,
        name = "Union Organization Islamic de France"
    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 13,
        name = "SDiyanet İşleri Başkanlığı, Turkey"
    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 14,
        name = "Spiritual Administration of Muslims of Russia"
    ),
    com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel.MethodDetails(
        method = 15,
        name = "Moonsighting Committee Worldwide"
    ),
//    MethodDetails(
//        method = 16,
//        name = "Dubai"
//    )
)

