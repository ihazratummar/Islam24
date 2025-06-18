package com.hazrat.islam24.core.domain.model.athkar


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MorningAkhtarData(
    @SerialName("arabicText")
    val arabicText: String,
    @SerialName("bismillah")
    val bismillah: String,
    @SerialName("bnTranslation")
    val bnTranslation: String,
    @SerialName("bnTransliteration")
    val bnTransliteration: String,
    @SerialName("count")
    val count: Int,
    @SerialName("enTranslation")
    val enTranslation: String,
    @SerialName("enTransliteration")
    val enTransliteration: String,
    @SerialName("number")
    val number: Int
)