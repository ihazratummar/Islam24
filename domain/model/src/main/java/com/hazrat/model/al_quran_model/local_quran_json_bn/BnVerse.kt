package com.hazrat.model.al_quran_model.local_quran_json_bn

import kotlinx.serialization.Serializable

@Serializable
data class BnVerse(
    val id: Int,
    val text: String,
    val translation: String
)