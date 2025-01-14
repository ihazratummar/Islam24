package com.hazrat.islam24.core.presentation.al_quran

import com.hazrat.islam24.core.domain.model.al_quran_model.FavoritesList
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.ArAyah
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.LocalQuranModelArItem

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

sealed interface QuranEvent {

    data class SaveLastRead(val surahNumber: Int, val ayahNumber: Int) : QuranEvent

    data object Refresh : QuranEvent

    data class SaveFavorite(val quranAr : LocalQuranModelArItem,val  arAyah :  ArAyah) : QuranEvent

}