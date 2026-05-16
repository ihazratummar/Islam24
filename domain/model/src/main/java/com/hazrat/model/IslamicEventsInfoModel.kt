package com.hazrat.model

data class IslamicEventsInfoModel(
    val holidays: String,
    val type: EventType,
    val gregorianDate: String,
    val hijriDate: String
)

enum class EventType {
    SPECIAL,
    NIGHT_PRAYER,
    URS,
    BIRTHDAY
}
