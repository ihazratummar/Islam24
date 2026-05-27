package com.hazrat.database.entity.quran

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "surah")
data class SurahEntity(
    @PrimaryKey val surahNumber: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameTransliterated: String,
    val type: String,
    val totalAyahs: Int
)

@Entity(
    tableName = "ayah",
    indices = [Index("surahNumber"), Index("globalAyahNumber", unique = true)]
)
data class AyahEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val globalAyahNumber: Int,  // 1–6236, maps directly to audio URL
    val arabicText: String,
    val englishTranslation: String,
    val transliteration: String,
    val isBookmarked: Boolean = false
)