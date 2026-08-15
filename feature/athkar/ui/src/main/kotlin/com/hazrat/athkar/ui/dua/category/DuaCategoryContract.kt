package com.hazrat.athkar.ui.dua.category

import androidx.compose.runtime.Immutable
import com.hazrat.model.DuaChapterWithCountModel
import com.hazrat.model.DuaItemModel
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.model.RecentReadDua

/**
 * @author hazratummar
 * Created on 30/05/26
 */
@Immutable
data class DuaCategoryState(
    val categories: List<HisnulMuslimCategory> = HisnulMuslimCategory.entries,
    val selectedCategory: HisnulMuslimCategory? = null,
    val categoryChapters: List<DuaChapterWithCountModel> = emptyList(),
    val allChapters: List<DuaChapterWithCountModel> = emptyList(),
    val searchResults: List<DuaChapterWithCountModel> = emptyList(),
    val bookmarkedDuas: List<DuaItemModel> = emptyList(),
    val recentDuas: List<RecentReadDua> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false
)

sealed interface DuaCategoryEvent {
    data class SearchDua(val query: String) : DuaCategoryEvent
    data class SelectCategory(val category: HisnulMuslimCategory) : DuaCategoryEvent
    data object ClearSelectedCategory : DuaCategoryEvent
    data class ToggleBookmark(val duaId: Int, val isBookmarked: Boolean) : DuaCategoryEvent
    data class DeleteRecent(val chapterId: Int) : DuaCategoryEvent
}