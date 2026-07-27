package com.hazrat.athkar.ui.dua.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.model.DuaCategoryModel
import com.hazrat.usecase.dua.GetDuaCategoryUseCase
import com.hazrat.usecase.dua.SearchAndGetDuaCategoriesUseCase
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


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeDuaCategory() {
        viewModelScope.launch {
            _state.map { it.searchText }
                .distinctUntilChanged()
                .debounce(300L.milliseconds)
                .flatMapLatest { query ->
                    if (query.isBlank()){
                        getDuaCategoryUseCase()
                    }else {
                        searchAndGetDuaCategoriesUseCase(query = query)
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