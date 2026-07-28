package com.hazrat.alQuran.ui.surah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.usecase.khatam.EndKhatamPlanUseCase
import com.hazrat.usecase.khatam.GetActiveKhatamPlanUseCase
import com.hazrat.usecase.khatam.GetKhatamHistoryUseCase
import com.hazrat.usecase.khatam.ResetKhatamPlanUseCase
import com.hazrat.usecase.khatam.StartKhatamPlanUseCase
import com.hazrat.usecase.khatam.UpdateKhatamTargetDateUseCase
import com.hazrat.usecase.quran.GetAllSurahListUseCase
import com.hazrat.usecase.quran.GetBookmarkedAyahsUseCase
import com.hazrat.usecase.quran.GetRecentSurahsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

class SurahViewModel(
    private val getAllSurahListUseCase: GetAllSurahListUseCase,
    private val getRecentSurahsUseCase: GetRecentSurahsUseCase,
    private val getBookmarkedAyahsUseCase: GetBookmarkedAyahsUseCase? = null,
    private val getActiveKhatamPlanUseCase: GetActiveKhatamPlanUseCase? = null,
    private val getKhatamHistoryUseCase: GetKhatamHistoryUseCase? = null,
    private val startKhatamPlanUseCase: StartKhatamPlanUseCase? = null,
    private val updateKhatamTargetDateUseCase: UpdateKhatamTargetDateUseCase? = null,
    private val resetKhatamPlanUseCase: ResetKhatamPlanUseCase? = null,
    private val endKhatamPlanUseCase: EndKhatamPlanUseCase? = null
) : ViewModel() {

    private val _surahState = MutableStateFlow(SurahState())
    val surahState: StateFlow<SurahState> = _surahState.asStateFlow()

    init {
        loadQuran()
        loadRecentReads()
        loadBookmarkedAyahs()
        loadKhatamData()
    }

    fun loadKhatamData() {
        viewModelScope.launch(Dispatchers.IO) {
            getActiveKhatamPlanUseCase?.invoke()?.collectLatest { activePlan ->
                _surahState.update { it.copy(activeKhatamPlan = activePlan) }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getKhatamHistoryUseCase?.invoke()?.collectLatest { history ->
                _surahState.update { it.copy(khatamHistory = history) }
            }
        }
    }

    fun loadBookmarkedAyahs() {
        viewModelScope.launch(Dispatchers.IO) {
            getBookmarkedAyahsUseCase?.invoke()?.collectLatest { bookmarks ->
                val grouped = bookmarks.groupBy { it.surahNumber }
                _surahState.update {
                    it.copy(
                        bookmarkedAyahs = bookmarks,
                        bookmarkedAyahsGrouped = grouped
                    )
                }
            }
        }
    }

    fun loadQuran() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllSurahListUseCase().collectLatest { quran ->
                _surahState.update { state ->
                    val query = state.searchQuery
                    val filtered = if (query.isBlank()) quran else quran.filter { surah ->
                        surah.nameEnglish.contains(query, ignoreCase = true) ||
                                surah.nameTransliterated.contains(query, ignoreCase = true) ||
                                surah.surahNumber.toString() == query.trim()
                    }
                    state.copy(
                        quranData = quran,
                        filteredQuranData = filtered
                    )
                }
            }
        }
    }

    fun loadRecentReads() {
        viewModelScope.launch(Dispatchers.IO) {
            getRecentSurahsUseCase().collectLatest { recents ->
                _surahState.update { it.copy(recentReads = recents) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _surahState.update { state ->
            val filtered = if (query.isBlank()) state.quranData else state.quranData.filter { surah ->
                surah.nameEnglish.contains(query, ignoreCase = true) ||
                        surah.nameTransliterated.contains(query, ignoreCase = true) ||
                        surah.surahNumber.toString() == query.trim()
            }
            state.copy(
                searchQuery = query,
                filteredQuranData = filtered
            )
        }
    }

    fun onSearchActiveChanged(isActive: Boolean) {
        _surahState.update { state ->
            if (!isActive) {
                state.copy(
                    isSearchActive = false,
                    searchQuery = "",
                    filteredQuranData = state.quranData
                )
            } else {
                state.copy(isSearchActive = true)
            }
        }
    }

    fun onTabSelected(tab: QuranTab) {
        _surahState.update { it.copy(selectedTab = tab) }
    }

    fun onViewModeChanged(mode: QuranViewMode) {
        _surahState.update { it.copy(selectedViewMode = mode) }
    }

    fun onOpenTargetDatePicker() {
        _surahState.update { it.copy(showTargetDatePickerSheet = true) }
    }

    fun onTargetDateSelected(targetTimestamp: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val activePlanId = _surahState.value.activeKhatamPlan?.id
            if (activePlanId != null && _surahState.value.showEditPlanSheet) {
                updateKhatamTargetDateUseCase?.invoke(activePlanId, targetTimestamp)
                _surahState.update { it.copy(showTargetDatePickerSheet = false, showEditPlanSheet = false) }
            } else {
                startKhatamPlanUseCase?.invoke(targetTimestamp)
                _surahState.update { it.copy(showTargetDatePickerSheet = false, showSettingCompletedSheet = true) }
            }
        }
    }

    fun onOpenEditPlanSheet() {
        _surahState.update { it.copy(showEditPlanSheet = true) }
    }

    fun onResetPlanClicked() {
        val activePlanId = _surahState.value.activeKhatamPlan?.id ?: return
        viewModelScope.launch(Dispatchers.IO) {
            resetKhatamPlanUseCase?.invoke(activePlanId)
            _surahState.update { it.copy(showEditPlanSheet = false) }
        }
    }

    fun onEndPlanClicked() {
        val activePlanId = _surahState.value.activeKhatamPlan?.id ?: return
        viewModelScope.launch(Dispatchers.IO) {
            endKhatamPlanUseCase?.invoke(activePlanId)
            _surahState.update { it.copy(showEditPlanSheet = false) }
        }
    }

    fun dismissSheets() {
        _surahState.update {
            it.copy(
                showTargetDatePickerSheet = false,
                showSettingCompletedSheet = false,
                showEditPlanSheet = false
            )
        }
    }
}
