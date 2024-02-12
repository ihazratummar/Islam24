package com.hazrat.islam24.domain.model.prayertime.prayersettingmodel

data class MethodDetails(
    val method: Int,
    val name: String
)

val prayerMethods = listOf(
    MethodDetails(
        method = 0,
        name = "Shia Ithna-Ashari"
    ),
    MethodDetails(
        method = 1,
        name = "University of Islamic Sciences, Karachi"
    ),
    MethodDetails(
        method = 2,
        name = "Islamic Society of North America"
    ),
    MethodDetails(
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
    MethodDetails(
        method = 11,
        name = "Majlis Ugama Islam Singapura, Singapore"
    ),
    MethodDetails(
        method = 12,
        name = "Union Organization Islamic de France"
    ),
    MethodDetails(
        method = 13,
        name = "SDiyanet İşleri Başkanlığı, Turkey"
    ),
    MethodDetails(
        method = 14,
        name = "Spiritual Administration of Muslims of Russia"
    ),
    MethodDetails(
        method = 15,
        name = "Moonsighting Committee Worldwide"
    ),
//    MethodDetails(
//        method = 16,
//        name = "Dubai"
//    )
)

