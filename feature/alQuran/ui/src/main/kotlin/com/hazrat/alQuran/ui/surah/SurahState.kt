package com.hazrat.alQuran.ui.surah

import com.hazrat.model.al_quran_model.SurahModel

data class SurahState(
    val quranData: List<SurahModel> = emptyList()
)
