package com.hazrat.model.al_quran_model

data class QuranMetaDataJuz(
    val juzData: List<X1>
)

data class X1(
    val start: Int,
    val end: Int
)


