package com.hazrat.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AthkarDto(
    @SerialName("data")
    val `data`: List<AthkarDataDto>,
    @SerialName("status")
    val status: String
)

@Serializable
data class AthkarDataDto(
    @SerialName("morning")
    val morning: List<MorningAkhtarDataDto>,
)


@Serializable
data class MorningAkhtarDataDto(
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