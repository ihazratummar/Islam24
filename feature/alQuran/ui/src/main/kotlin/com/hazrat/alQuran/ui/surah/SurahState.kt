package com.hazrat.alQuran.ui.surah

import com.hazrat.model.KhatamPlanModel
import com.hazrat.model.al_quran_model.AyahModel
import com.hazrat.model.al_quran_model.RecentReadSurah
import com.hazrat.model.al_quran_model.SurahModel

enum class QuranTab {
    READ,
    KHATAM,
    BOOKMARK
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
    val juzList: List<JuzItem> = JuzDataHelper.getJuzList(),
    val bookmarkedAyahs: List<AyahModel> = emptyList(),
    val bookmarkedAyahsGrouped: Map<Int, List<AyahModel>> = emptyMap(),
    val activeKhatamPlan: KhatamPlanModel? = null,
    val khatamHistory: List<KhatamPlanModel> = emptyList(),
    val showTargetDatePickerSheet: Boolean = false,
    val showSettingCompletedSheet: Boolean = false,
    val showEditPlanSheet: Boolean = false
)
