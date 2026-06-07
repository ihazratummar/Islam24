package com.hazrat.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllahNamesDto(
    @SerialName("code")
    val code: Int,
    @SerialName("data")
    val `data`: List<AllahNamesDataDto>,
    @SerialName("status")
    val status: String
)

@Serializable
data class AllahNamesDataDto(
    @SerialName("en")
    val en: AllahNamesEnDto,
    @SerialName("bn")
    val bn: AllahNamesBnDto,
    @SerialName("found")
    val found: String,
    @SerialName("name")
    val name: String,
    @SerialName("number")
    val number: Int,
    @SerialName("transliteration")
    val transliteration: String,
    @SerialName("bntransliteration")
    val bntransliteration: String
)

@Serializable
data class AllahNamesEnDto(
    @SerialName("desc")
    val desc: String,
    @SerialName("meaning")
    val meaning: String
)

@Serializable
data class AllahNamesBnDto(
    @SerialName("desc")
    val desc: String?= null,
    @SerialName("meaning")
    val meaning: String
)
