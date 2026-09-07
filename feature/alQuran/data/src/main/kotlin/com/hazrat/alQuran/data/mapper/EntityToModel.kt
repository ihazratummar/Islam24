package com.hazrat.alQuran.data.mapper

import com.hazrat.database.entity.quran.AyahWithBookmarkEntity
import com.hazrat.database.entity.quran.SurahEntity
import com.hazrat.model.quran.AyahModel
import com.hazrat.model.quran.SurahModel

/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun SurahEntity.toModel() : SurahModel {
    return SurahModel(
        nameArabic = this.nameArabic,
        nameEnglish = this.nameEnglish,
        nameTransliterated = this.nameTransliterated,
        type = this.type,
        totalAyahs = this.totalAyahs,
        surahNumber = this.surahNumber,
    )
}

fun List<SurahEntity>.toModelList() : List<SurahModel> {
    return this.map { it.toModel() }
}

fun AyahWithBookmarkEntity.toModel(): AyahModel {
    return AyahModel(
        id = this.id,
        surahNumber = this.surahNumber,
        globalAyahNumber = this.globalAyahNumber,
        arabicText = this.arabicText,
        englishTranslation = this.englishTranslation,
        transliteration = this.transliteration,
        isBookmarked = this.isBookmarked,
        ayahNumber = this.ayahNumber,
        tajweedText = this.tajweedText,
        bnMuhiuddin = this.bnMuhiuddin,
        bnTaisirul = this.bnTaisirul,
        bnMujibur = this.bnMujibur,
        bnTransliteration = this.bnTransliteration
    )
}

fun List<AyahWithBookmarkEntity>.toAyahModelList(): List<AyahModel> {
    return this.map { it.toModel() }
}

