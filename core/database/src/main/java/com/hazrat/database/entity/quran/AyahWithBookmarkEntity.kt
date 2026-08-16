package com.hazrat.database.entity.quran

/**
 * Database query projection combining Ayah with its bookmark status.
 * Stays strictly within the core:database layer.
 *
 * @author hazratummar
 */
data class AyahWithBookmarkEntity(
    val id: Int = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val globalAyahNumber: Int,
    val arabicText: String,
    val englishTranslation: String,
    val transliteration: String,
    val isBookmarked: Boolean = false,
    val tajweedText: String = ""
)
