package com.hazrat.alQuran.data.mapper


import com.hazrat.database.entity.quran.AyahEntity
import com.hazrat.database.entity.quran.SurahEntity
import com.hazrat.model.al_quran_model.AyahModel
import com.hazrat.model.al_quran_model.SurahModel


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


fun AyahEntity.toAyahModel() : AyahModel {
    return AyahModel(
        id = this.id,
        surahNumber = this.surahNumber,
        globalAyahNumber = this.globalAyahNumber,
        arabicText = this.arabicText,
        englishTranslation = this.englishTranslation,
        transliteration = this.transliteration,
        isBookmarked = this.isBookmarked,
        ayahNumber = this.ayahNumber
    )
}

fun List<AyahEntity>.toAyahModelList() : List<AyahModel> {
    return this.map { it.toAyahModel() }
}