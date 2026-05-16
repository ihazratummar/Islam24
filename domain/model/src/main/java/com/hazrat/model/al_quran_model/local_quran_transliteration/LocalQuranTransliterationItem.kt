package com.hazrat.model.al_quran_model.local_quran_transliteration

import kotlinx.serialization.Serializable

@Serializable
data class LocalQuranTransliterationItem(
    val id: Int,
    val name: String,
    val total_verses: Int,
    val translation: String,
    val transliteration: String,
    val type: String,
    val verses: List<TransliterationVerse>
)