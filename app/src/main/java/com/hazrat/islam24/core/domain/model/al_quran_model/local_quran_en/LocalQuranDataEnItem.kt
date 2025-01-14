package com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_en

data class LocalQuranDataEnItem(
    val id: Int,
    val name: String,
    val total_verses: Int,
    val translation: String,
    val transliteration: String,
    val type: String,
    val verses: List<EnVerse>
)