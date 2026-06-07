package com.hazrat.athkar.ui.dua.dua_details

import androidx.compose.runtime.Immutable
import com.hazrat.model.DuaItemModel


/**
 * @author hazratummar
 * Created on 30/05/26
 */
 



@Immutable
data class DuaItemState(
    val duaItemLis : List<DuaItemModel> = emptyList()
)