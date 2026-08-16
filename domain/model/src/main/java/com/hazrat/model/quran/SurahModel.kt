package com.hazrat.model.quran

data class SurahModel(
    val surahNumber: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameTransliterated: String,
    val type: String,
    val totalAyahs : Int
)


