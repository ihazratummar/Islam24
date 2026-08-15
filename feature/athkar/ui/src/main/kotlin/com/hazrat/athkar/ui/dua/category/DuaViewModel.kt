package com.hazrat.athkar.ui.dua.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.usecase.dua.DeleteRecentDuaUseCase
import com.hazrat.usecase.dua.GetAllChaptersUseCase
import com.hazrat.usecase.dua.GetBookmarkedDuasUseCase
import com.hazrat.usecase.dua.GetCategoryChaptersUseCase
import com.hazrat.usecase.dua.GetRecentDuasUseCase
import com.hazrat.usecase.dua.SearchChaptersUseCase
import com.hazrat.usecase.dua.ToggleDuaBookmarkUseCase
import com.hazrat.utils.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * ViewModel for Dua & Azkar feature hub, categories, chapters, bookmarks, and recents.
 * @author hazratummar
 * Created on 30/05/26
 */
class DuaViewModel(
    private val getAllChaptersUseCase: GetAllChaptersUseCase,
    private val getCategoryChaptersUseCase: GetCategoryChaptersUseCase,
    private val searchChaptersUseCase: SearchChaptersUseCase,
    private val getBookmarkedDuasUseCase: GetBookmarkedDuasUseCase,
    private val toggleDuaBookmarkUseCase: ToggleDuaBookmarkUseCase,
    private val getRecentDuasUseCase: GetRecentDuasUseCase,
    private val deleteRecentDuaUseCase: DeleteRecentDuaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DuaCategoryState())
    val state: StateFlow<DuaCategoryState> = _state.asStateFlow()

    init {
        loadAllChapters()
        observeSearch()
        observeBookmarks()
        observeRecents()
    }

    private fun loadAllChapters() {
        viewModelScope.launch {
            getAllChaptersUseCase().collectLatest { result ->
                if (result is Result.Success) {
                    _state.update { it.copy(allChapters = result.data) }
                }
            }
        }
    }

    private fun observeBookmarks() {
        viewModelScope.launch {
            getBookmarkedDuasUseCase().collectLatest { result ->
                if (result is Result.Success) {
                    _state.update { it.copy(bookmarkedDuas = result.data) }
                }
            }
        }
    }

    private fun observeRecents() {
        viewModelScope.launch {
            getRecentDuasUseCase().collectLatest { recents ->
                _state.update { it.copy(recentDuas = recents) }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            _state.map { it.searchText }
                .distinctUntilChanged()
                .debounce(300L.milliseconds)
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        getAllChaptersUseCase()
                    } else {
                        searchChaptersUseCase(query.trim())
                    }
                }.collectLatest { result ->
                    if (result is Result.Success) {
                        _state.update { it.copy(searchResults = result.data) }
                    }
                }
        }
    }

    fun event(event: DuaCategoryEvent) {
        when (event) {
            is DuaCategoryEvent.SearchDua -> {
                _state.update { it.copy(searchText = event.query) }
            }

            is DuaCategoryEvent.SelectCategory -> {
                _state.update { it.copy(selectedCategory = event.category) }
                loadChaptersForCategory(event.category)
            }

            is DuaCategoryEvent.ClearSelectedCategory -> {
                _state.update { it.copy(selectedCategory = null, categoryChapters = emptyList()) }
            }

            is DuaCategoryEvent.ToggleBookmark -> {
                viewModelScope.launch {
                    toggleDuaBookmarkUseCase(event.duaId, event.isBookmarked)
                }
            }

            is DuaCategoryEvent.DeleteRecent -> {
                viewModelScope.launch {
                    deleteRecentDuaUseCase(event.chapterId)
                }
            }
        }
    }

    fun loadChaptersForCategory(category: HisnulMuslimCategory) {
        viewModelScope.launch {
            getCategoryChaptersUseCase(category).collectLatest { result ->
                if (result is Result.Success) {
                    _state.update { it.copy(categoryChapters = result.data) }
                }
            }
        }
    }
}