package com.hazrat.athkar.ui.dua.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.model.DuaCategoryModel
import com.hazrat.usecase.dua.GetDuaCategoryUseCase
import com.hazrat.usecase.dua.SearchAndGetDuaCategoriesUseCase
import com.hazrat.utils.result.Result
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


/**
 * @author hazratummar
 * Created on 30/05/26
 */

class DuaViewModel(
    private val getDuaCategoryUseCase: GetDuaCategoryUseCase,
    private val searchAndGetDuaCategoriesUseCase: SearchAndGetDuaCategoriesUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(DuaCategoryState())
    val state : StateFlow<DuaCategoryState> = _state.asStateFlow()


    init {
        observeDuaCategory()
        Log.d("DuaViewModel", "Dua ViewModel Loaded")
    }


    private fun observeDuaCategory() {
        viewModelScope.launch {
            _state.map { it.searchText }
                .distinctUntilChanged()
                .debounce(300L)
                .flatMapLatest { qeury ->
                    if (qeury.isBlank()){
                        getDuaCategoryUseCase()
                    }else {
                        searchAndGetDuaCategoriesUseCase(query = qeury)
                    }
                }.collectLatest { result ->
                    when(result) {
                        is Result.Error -> {}
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    duaCategory = result.data
                                )
                            }
                            Log.d("DuaViewModel", "Data ${result.data}")
                        }
                    }
                }
        }
    }

    fun event(event: DuaCategoryEvent) {
        when(event) {
            is DuaCategoryEvent.SearchDuaCategory -> {
                _state.update {
                    it.copy(
                        searchText = event.query
                    )
                }
            }
        }
    }


}