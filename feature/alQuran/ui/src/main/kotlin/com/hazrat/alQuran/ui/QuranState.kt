package com.hazrat.alQuran.ui

import com.hazrat.model.al_quran_model.FavoritesList
import com.hazrat.model.al_quran_model.QuranMetaDataJuz
import com.hazrat.model.al_quran_model.local_quran_ar.LocalQuranModelArItem
import com.hazrat.model.al_quran_model.local_quran_en.LocalQuranDataEnItem
import com.hazrat.model.al_quran_model.local_quran_json_bn.LocalQuranDataItemBn
import com.hazrat.model.al_quran_model.local_quran_transliteration.LocalQuranTransliterationItem

data class QuranState(
    val lastReadSurah: Int? = null,
    val lastReadAyah: Int? = null,
    val lastReadSurahName: String? = null,
    val arQuranData: List<LocalQuranModelArItem>? = null,
    val quranTransliterationData: List<LocalQuranTransliterationItem>? = null,
    val quranEnData: List<LocalQuranDataEnItem>? = null,
    val quranBnData: List<LocalQuranDataItemBn>? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val needsRefresh: Boolean = true,
    val juzData: QuranMetaDataJuz? = null,
    val favoritesList: FavoritesList? = null,
    val ayahFavoriteStatus: Map<Pair<Int, Int>, Boolean> = emptyMap(),
    val isAyahDropDownOpen: Boolean = false,
    val isQuranSettingDiloagOpen : Boolean = false
)
