package com.hazrat.model.al_quran_model

import kotlinx.serialization.Serializable

@Serializable
data class QuranMetaDataJuz(
    val juzData: List<X1>
)

@Serializable
data class X1(
    val start: Int,
    val end: Int
)
