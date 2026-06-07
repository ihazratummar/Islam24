package com.hazrat.athkar.ui.dua.dua_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.usecase.dua.GetDuaItemListUseCase
import com.hazrat.utils.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * @author hazratummar
 * Created on 30/05/26
 */

class DuaItemViewModel (
    private val duaCategoryId: Int,
    private val getDuaItemListUseCase: GetDuaItemListUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(DuaItemState())
    val state : StateFlow<DuaItemState> = _state.asStateFlow()



    init {
        loadDuaItem()
    }


    private fun loadDuaItem() {
        viewModelScope.launch {
            getDuaItemListUseCase.invoke(categoryId = duaCategoryId).collectLatest {result ->
                when(result) {
                    is Result.Error -> {}
                    is Result.Success-> {
                        _state.update {
                            it.copy(
                                duaItemLis = result.data
                            )
                        }
                    }
                }

            }
        }
    }

}