package com.hazrat.model.al_quran_model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteAyah(
    val surahNumber: Int,
    val ayahNumber: Int
)

typealias FavoritesList = List<FavoriteAyah>

