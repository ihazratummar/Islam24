package com.hazrat.islam24.core.domain.model.namesofallah

data class NameOfAllahData(
    val en: En,
    val bn: Bn,
    val found: String,
    val name: String,
    val number: Int,
    val transliteration: String,
    val bntransliteration: String
)

data class Bn(
    val desc: String,
    val meaning: String
)

data class En(
    val desc: String,
    val meaning: String
)