package com.hazrat.model.al_quran_model.local_quran_en

import kotlinx.serialization.Serializable

@Serializable
data class LocalQuranDataEnItem(
    val id: Int,
    val name: String,
    val total_verses: Int,
    val translation: String,
    val transliteration: String,
    val type: String,
    val verses: List<EnVerse>
)