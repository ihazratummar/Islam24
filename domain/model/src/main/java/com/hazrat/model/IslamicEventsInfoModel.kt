package com.hazrat.model

data class IslamicEventsInfoModel(
    val holidays: String,
    val type: EventType,
    val gregorianDate: String,
    val hijriDate: String,
    val timestamp: Long?
)

enum class EventType {
    SPECIAL {
        override fun toString(): String {
            return "Special"
        }
    },
    NIGHT_PRAYER {
        override fun toString(): String {
            return "Night Prayer"
        }
    },
    URS{
        override fun toString(): String {
            return "Urs"
        }
    },
    BIRTHDAY{
        override fun toString(): String {
            return "Birthday"
        }
    },
    HAJJ {
        override fun toString(): String {
            return "Hajj"
        }
    },
    WEEKLY {
        override fun toString(): String {
            return "Weekly"
        }
    },
    JUMMA {
        override fun toString(): String {
            return "Jummah"
        }
    }
}
