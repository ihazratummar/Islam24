package com.hazrat.model.prayersettingmodel

import android.R
import kotlinx.serialization.Serializable

@Serializable
data class CalculationMethodDetails(
    val method: Int,
    val name: String,
    val farjAngle: String = "18°",
    val ishaAngle: String = "18°",
    val region: String = "South Asia",
)

val prayerMethods = listOf(
    CalculationMethodDetails(
        method = 0,
        name = "Shia Ithna-Ashari",
        farjAngle = "16°",
        ishaAngle = "14°",
        region = "Shia"
    ),
    CalculationMethodDetails(
        method = 1,
        name = "University of Islamic Sciences, Karachi"
    ),
    CalculationMethodDetails(
        method = 2,
        name = "Islamic Society of North America",
        farjAngle = "15°",
        ishaAngle = "15°",
        region = "America"
    ),
    CalculationMethodDetails(
        method = 3,
        name = "Muslim World League",
        farjAngle = "18°",
        ishaAngle = "17°",
        region = "Global"
    ),
    CalculationMethodDetails(
        method = 4,
        name = "Umm Al-Qura University, Makkah",
        farjAngle = "18.5°",
        ishaAngle = "90 Min",
        region = "Middle East"
    ),
    CalculationMethodDetails(
        method = 5,
        name = "Egyptian General Authority of Survey",
        region = "Africa",
        farjAngle = "19.5°",
        ishaAngle = "17.5°"
    ),
    CalculationMethodDetails(
        method = 7,
        name = "Institute of Geophysics, University of Tehran",
        farjAngle = "17.5°",
        ishaAngle = "14°",
        region = "Iran"
    ),
    CalculationMethodDetails(
        method = 8,
        name = "Gulf Region",
        farjAngle = "19.5°",
        ishaAngle = "90 min",
        region = "Middle East"
    ),
    CalculationMethodDetails(
        method = 9,
        name = "Kuwait",
        farjAngle = "18°",
        ishaAngle = "17.5°",
        region = "Middle East"
    ),
    CalculationMethodDetails(
        method = 10,
        name = "Qatar",
        farjAngle = "18°",
        ishaAngle = "90 min",
        region = "Middle East"
    ),
    CalculationMethodDetails(
        method = 11,
        name = "Majlis Ugama Islam Singapura, Singapore",
        farjAngle = "20°",
        ishaAngle = "18°",
        region = "Southeast Asia"
    ),
    CalculationMethodDetails(
        method = 12,
        name = "Union Organization Islamic de France",
        farjAngle = "12°",
        ishaAngle = "12°",
        region = "France"
    ),
    CalculationMethodDetails(
        method = 13,
        name = "Diyanet İşleri Başkanlığı, Turkey",
        farjAngle = "18°",
        ishaAngle = "17°",
        region = "Turkey"
    ),
    CalculationMethodDetails(
        method = 14,
        name = "Spiritual Administration of Muslims of Russia",
        farjAngle = "16°",
        ishaAngle = "15°",
        region = "Russia"
    ),
    CalculationMethodDetails(
        method = 15,
        name = "Moonsighting Committee Worldwide",

    ),
    CalculationMethodDetails(
        method = 16,
        name = "Dubai",
        farjAngle = "16.2°",
        ishaAngle = "16.2°",
        region = "Middle East"
    ),
    CalculationMethodDetails(
        method = 17,
        name = "Jabatan Kemajuan Islam Malaysia",
        farjAngle = "20°",
        ishaAngle = "18°",
        region = "Southeast Asia"
    ),
    CalculationMethodDetails(
        method = 18,
        name = "Tunisia",
        region = "North Africa"
    )
    ,
    CalculationMethodDetails(
        method = 19,
        name = "Algeria",
        ishaAngle = "17°",
        region = "North Africa"
    ),
    CalculationMethodDetails(
        method = 20,
        name = "KEMENAG - Kementerian Agama Republik Indonesia",
        farjAngle = "20°",
        region = "Southeast ASia"
    ),
    CalculationMethodDetails(
        method = 21,
        name = "Morocco",
        farjAngle = "19°",
        ishaAngle = "17°",
        region = "North Africa"
    ),
    CalculationMethodDetails(
        method = 22,
        name = "Comunidade Islamica de Lisboa",
        ishaAngle = "77 min",
        region = "Southern European"
    ),
    CalculationMethodDetails(
        method = 23,
        name = "Ministry of Awqaf, Islamic Affairs and Holy Places, Jordan",
        region = "Middle East"
    )

)


enum class JuristicMethod (
    val index: Int, val description : String
) {
    STANDARD (index = 0, description = "Asr starts when shadow equals object height"){
        override fun toString(): String {
            return "Shafi, Maliki, Hanbali"
        }
    },
    HANAFI(index = 1, description = "Asr starts when shadow equals twice object heigh"){
        override fun toString(): String {
            return "Hanafi"
        }
    }
}
