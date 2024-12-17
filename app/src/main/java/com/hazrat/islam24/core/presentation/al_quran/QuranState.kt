package com.hazrat.islam24.core.presentation.al_quran

import com.hazrat.islam24.core.domain.model.al_quran_model.FavoritesList
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.LocalQuranModelArItem
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_en.LocalQuranDataEnItem
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_json_bn.LocalQuranDataItemBn
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_transliteration.LocalQuranTransliterationItem
import com.hazrat.islam24.core.domain.model.al_quran_model.meta_data_juz.QuranMetaDataJuz

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
    val ayahFavoriteStatus: Map<Pair<Int, Int>, Boolean> = emptyMap()
)
