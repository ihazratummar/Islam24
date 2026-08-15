package com.hazrat.athkar.ui.dua.dua_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.usecase.dua.GetAllChaptersUseCase
import com.hazrat.usecase.dua.GetDuaItemListUseCase
import com.hazrat.usecase.dua.SaveRecentDuaUseCase
import com.hazrat.usecase.dua.ToggleDuaBookmarkUseCase
import com.hazrat.utils.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author hazratummar
 * Created on 30/05/26
 */
class DuaItemViewModel(
    private val duaCategoryId: Int,
    private val getDuaItemListUseCase: GetDuaItemListUseCase,
    private val saveRecentDuaUseCase: SaveRecentDuaUseCase,
    private val getAllChaptersUseCase: GetAllChaptersUseCase,
    private val toggleDuaBookmarkUseCase: ToggleDuaBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DuaItemState(chapterId = duaCategoryId))
    val state: StateFlow<DuaItemState> = _state.asStateFlow()

    init {
        loadChapterInfo()
        loadDuaItems()
    }

    private fun loadChapterInfo() {
        viewModelScope.launch {
            val chaptersResult = getAllChaptersUseCase().firstOrNull()
            val chapter = (chaptersResult as? Result.Success)?.data?.firstOrNull { it.id == duaCategoryId }
            val title = chapter?.title ?: "Chapter $duaCategoryId"
            val count = chapter?.duaCount ?: 0

            _state.update {
                it.copy(
                    chapterTitle = title,
                    totalDuas = count
                )
            }

            val formattedDate = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())
            saveRecentDuaUseCase(
                chapterId = duaCategoryId,
                title = title,
                duaCount = count,
                formattedDate = formattedDate
            )
        }
    }

    private fun loadDuaItems() {
        viewModelScope.launch {
            getDuaItemListUseCase.invoke(categoryId = duaCategoryId).collectLatest { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                duaItemList = result.data,
                                totalDuas = if (it.totalDuas == 0) result.data.size else it.totalDuas
                            )
                        }
                    }
                }
            }
        }
    }

    fun event(event: DuaItemEvent) {
        when (event) {
            is DuaItemEvent.SelectDuaForMenu -> {
                _state.update { it.copy(selectedDuaForMenu = event.dua) }
            }

            is DuaItemEvent.SelectDuaForShare -> {
                _state.update {
                    it.copy(
                        selectedDuaForMenu = null,
                        selectedDuaForShare = event.dua
                    )
                }
            }

            is DuaItemEvent.ToggleBookmark -> {
                viewModelScope.launch {
                    toggleDuaBookmarkUseCase(event.duaId, event.isBookmarked)
                    // Instantly update local list state
                    _state.update { currentState ->
                        val updatedList = currentState.duaItemList.map { item ->
                            if (item.id == event.duaId) item.copy(isBookmarked = event.isBookmarked) else item
                        }
                        currentState.copy(
                            duaItemList = updatedList,
                            selectedDuaForMenu = if (currentState.selectedDuaForMenu?.id == event.duaId) {
                                currentState.selectedDuaForMenu.copy(isBookmarked = event.isBookmarked)
                            } else {
                                currentState.selectedDuaForMenu
                            }
                        )
                    }
                }
            }
        }
    }
}