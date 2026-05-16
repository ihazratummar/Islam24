package com.hazrat.model.al_quran_model.local_quran_transliteration

import kotlinx.serialization.Serializable

@Serializable
data class TransliterationVerse(
    val id: Int,
    val text: String,
    val transliteration: String
)