package com.hazrat.alQuran.ui

import com.hazrat.model.al_quran_model.local_quran_ar.ArAyah
import com.hazrat.model.al_quran_model.local_quran_ar.LocalQuranModelArItem

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

sealed interface QuranEvent {

    data class SaveLastRead(val surahNumber: Int, val ayahNumber: Int) : QuranEvent

    data object Refresh : QuranEvent

    data class SaveFavorite(val quranAr : LocalQuranModelArItem,val  arAyah :  ArAyah) : QuranEvent

    data object OpenQuranSettingDialog : QuranEvent

    data object AyahDropDownClick: QuranEvent



}