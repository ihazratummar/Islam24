package com.hazrat.alQuran.ui.ayah

import androidx.compose.runtime.Stable
import com.hazrat.model.al_quran_model.AyahModel
import com.hazrat.model.al_quran_model.SurahModel


/**
 * @author hazratummar
 * Created on 27/05/26
 */
 
@Stable
data class AyahState(
    val ayahs : List<AyahModel>  = emptyList(),
    val surahDetails: SurahModel ? = null
)