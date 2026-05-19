package com.hazrat.model

import kotlinx.serialization.Serializable

@Serializable
enum class IslamicEventType {
    RAMADAN{
        override fun toString(): String {
            return "RAMADAN"
        }
    },
    EID_UL_FITR{
        override fun toString(): String {
            return "EID UL FITR"
        }
    },
    EID_UL_ADHA {
        override fun toString(): String {
            return "EID UL ADHA"
        }
    }
}
