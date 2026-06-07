package com.hazrat.model.al_quran_model

data class AyahModel(
    val id: Int,
    val surahNumber: Int,
    val globalAyahNumber: Int,
    val arabicText: String,
    val ayahNumber: Int,
    val englishTranslation : String,
    val transliteration : String,
    val isBookmarked : Boolean = false,
    val tajweedText: String = ""
)
