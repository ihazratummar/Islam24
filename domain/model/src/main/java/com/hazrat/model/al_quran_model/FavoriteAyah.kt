package com.hazrat.model.al_quran_model

data class FavoriteAyah(
    val surahNumber: Int,
    val ayahNumber: Int
)

typealias FavoritesList = List<FavoriteAyah>

