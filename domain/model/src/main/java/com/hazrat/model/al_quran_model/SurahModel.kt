package com.hazrat.model.al_quran_model

data class SurahModel(
    val surahNumber: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameTransliterated: String,
    val type: String,
    val totalAyahs : Int
)


