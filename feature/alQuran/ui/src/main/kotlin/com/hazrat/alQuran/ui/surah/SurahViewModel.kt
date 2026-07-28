package com.hazrat.alQuran.ui.surah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getBookmarkedAyahsUseCase: GetBookmarkedAyahsUseCase? = null
) : ViewModel() {

    private val _surahState = MutableStateFlow(SurahState())
    val surahState: StateFlow<SurahState> = _surahState.asStateFlow()

    init {
        loadQuran()
        loadRecentReads()
        loadBookmarkedAyahs()
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
}
