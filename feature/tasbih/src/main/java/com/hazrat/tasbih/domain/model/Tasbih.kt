package com.hazrat.tasbih.domain.model

data class Tasbih(
    val id: Int = 0,
    val arabicText: String,
    val transliteration: String,
    val translatedName: String,
    val defaultTarget: Int = 33,
    val currentCount: Int = 0,
    val totalLifetimeCount: Int = 0,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)
