package com.hazrat.athkar.ui.dua.category

import androidx.compose.runtime.Immutable
import com.hazrat.model.DuaCategoryModel


/**
 * @author hazratummar
 * Created on 30/05/26
 */
 


@Immutable
data class DuaCategoryState(
    val duaCategory : List<DuaCategoryModel> = emptyList(),
    val searchText: String = ""
)

sealed interface DuaCategoryEvent {
    data class SearchDuaCategory (val query : String) : DuaCategoryEvent
}