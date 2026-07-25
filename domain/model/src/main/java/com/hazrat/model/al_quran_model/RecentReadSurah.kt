package com.hazrat.model.al_quran_model

data class RecentReadSurah(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val formattedDate: String,
    val timestamp: Long = System.currentTimeMillis()
)
