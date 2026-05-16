package com.hazrat.model.al_quran_model.local_quran_en

import kotlinx.serialization.Serializable

@Serializable
data class EnVerse(
    val id: Int,
    val text: String,
    val translation: String
)