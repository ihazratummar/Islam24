package com.hazrat.athkar.ui.dua.dua_details

import androidx.compose.runtime.Immutable
import com.hazrat.model.DuaItemModel

/**
 * @author hazratummar
 * Created on 30/05/26
 */
@Immutable
data class DuaItemState(
    val chapterId: Int = 0,
    val chapterTitle: String = "",
    val totalDuas: Int = 0,
    val duaItemList: List<DuaItemModel> = emptyList(),
    val selectedDuaForMenu: DuaItemModel? = null,
    val selectedDuaForShare: DuaItemModel? = null,
    val isLoading: Boolean = false
)

sealed interface DuaItemEvent {
    data class SelectDuaForMenu(val dua: DuaItemModel?) : DuaItemEvent
    data class SelectDuaForShare(val dua: DuaItemModel?) : DuaItemEvent
    data class ToggleBookmark(val duaId: Int, val isBookmarked: Boolean) : DuaItemEvent
}