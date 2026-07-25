package com.hazrat.alQuran.ui.surah

import com.hazrat.model.al_quran_model.RecentReadSurah
import com.hazrat.model.al_quran_model.SurahModel

enum class QuranTab {
    READ,
    MY_PROGRESS
}

enum class QuranViewMode {
    SURA,
    JUZ
}

data class SurahState(
    val quranData: List<SurahModel> = emptyList(),
    val filteredQuranData: List<SurahModel> = emptyList(),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val selectedTab: QuranTab = QuranTab.READ,
    val selectedViewMode: QuranViewMode = QuranViewMode.SURA,
    val recentReads: List<RecentReadSurah> = emptyList(),
    val juzList: List<JuzItem> = JuzDataHelper.getJuzList()
)
