package com.hazrat.model.al_quran_model.local_quran_json_bn

import kotlinx.serialization.Serializable

@Serializable
data class LocalQuranDataItemBn(
    val id: Int,
    val name: String,
    val total_verses: Int,
    val translation: String,
    val transliteration: String,
    val type: String,
    val verses: List<BnVerse>
)