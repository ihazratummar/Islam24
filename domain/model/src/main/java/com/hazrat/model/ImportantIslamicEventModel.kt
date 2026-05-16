package com.hazrat.model

import kotlinx.serialization.Serializable

@Serializable
data class ImportantIslamicEventModel(
    val gregorianTimestamp: Long = 0L,
    val title: String = "",
    val hijriDay: Int = 0,
    val hijriMonthName: String = "",
    val hijriMonthNumber: Int = 9,
    val hijriYear: Int = 0,
    val hijriYearAd: String = "AH" ,
    val type: IslamicEventType = IslamicEventType.RAMADAN
)


@Serializable
enum class IslamicEventType {
    RAMADAN,
    EID_UL_FITR,
    EID_UL_ADHA
}
