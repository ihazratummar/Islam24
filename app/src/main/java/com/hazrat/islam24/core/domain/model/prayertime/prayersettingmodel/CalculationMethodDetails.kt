package com.hazrat.islam24.core.domain.model.prayertime.prayersettingmodel

data class CalculationMethodDetails(
    val method: Int,
    val name: String
)

val prayerMethods = listOf(
    CalculationMethodDetails(
        method = 0,
        name = "Shia Ithna-Ashari"
    ),
    CalculationMethodDetails(
        method = 1,
        name = "University of Islamic Sciences, Karachi"
    ),
    CalculationMethodDetails(
        method = 2,
        name = "Islamic Society of North America"
    ),
    CalculationMethodDetails(
        method = 3,
        name = "Muslim World League"
    ),
    CalculationMethodDetails(
        method = 4,
        name = "Umm Al-Qura University, Makkah"
    ),
    CalculationMethodDetails(
        method = 5,
        name = "Egyptian General Authority of Survey"
    ),
    CalculationMethodDetails(
        method = 7,
        name = "Institute of Geophysics, University of Tehran"
    ),
    CalculationMethodDetails(
        method = 8,
        name = "Gulf Region"
    ),
    CalculationMethodDetails(
        method = 9,
        name = "Kuwait"
    ),
    CalculationMethodDetails(
        method = 10,
        name = "Qatar"
    ),
    CalculationMethodDetails(
        method = 11,
        name = "Majlis Ugama Islam Singapura, Singapore"
    ),
    CalculationMethodDetails(
        method = 12,
        name = "Union Organization Islamic de France"
    ),
    CalculationMethodDetails(
        method = 13,
        name = "Diyanet İşleri Başkanlığı, Turkey"
    ),
    CalculationMethodDetails(
        method = 14,
        name = "Spiritual Administration of Muslims of Russia"
    ),
    CalculationMethodDetails(
        method = 15,
        name = "Moonsighting Committee Worldwide"
    ),
    CalculationMethodDetails(
        method = 16,
        name = "Dubai"
    ),
    CalculationMethodDetails(
        method = 17,
        name = "Jabatan Kemajuan Islam Malaysia"
    ),
    CalculationMethodDetails(
        method = 18,
        name = "Tunisia"
    )
    ,
    CalculationMethodDetails(
        method = 19,
        name = "Algeria"
    ),
    CalculationMethodDetails(
        method = 20,
        name = "KEMENAG - Kementerian Agama Republik Indonesia"
    ),
    CalculationMethodDetails(
        method = 21,
        name = "Morocco"
    ),
    CalculationMethodDetails(
        method = 22,
        name = "Comunidade Islamica de Lisboa"
    ),
    CalculationMethodDetails(
        method = 23,
        name = "Ministry of Awqaf, Islamic Affairs and Holy Places, Jordan"
    )

)

