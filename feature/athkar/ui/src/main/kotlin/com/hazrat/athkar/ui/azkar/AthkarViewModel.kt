package com.hazrat.athkar.ui.azkar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.model.DuaItemModel
import com.hazrat.usecase.dua.GetDuaItemListUseCase
import com.hazrat.utils.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.hazrat.datastore.DataStorePreference
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class AthkarItemState(
    val item: DuaItemModel,
    val currentCount: Int = 0,
    val isFreeCount: Boolean = false,
    val isCompleted: Boolean = false
)

data class AthkarUiState(
    val morningAthkar: List<AthkarItemState> = emptyList(),
    val eveningAthkar: List<AthkarItemState> = emptyList(),
    val isLoading: Boolean = false
)

class AthkarViewModel (
    private val getDuaItemListUseCase: GetDuaItemListUseCase,
    private val dataStorePreference: DataStorePreference? = null
) : ViewModel() {

    private val todayDateStr: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    private val _morningList = MutableStateFlow<List<DuaItemModel>>(emptyList())
    private val _eveningList = MutableStateFlow<List<DuaItemModel>>(emptyList())
    private val _countsMap = MutableStateFlow<Map<Int, Int>>(emptyMap())
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<AthkarUiState> = combine(
        _morningList,
        _eveningList,
        _countsMap,
        _isLoading
    ) { morning, evening, counts, loading ->
        AthkarUiState(
            morningAthkar = morning.map { item ->
                val current = counts[item.id] ?: 0
                val freeCount = item.repeatCount <= 0
                AthkarItemState(
                    item = item,
                    currentCount = current,
                    isFreeCount = freeCount,
                    isCompleted = !freeCount && current >= item.repeatCount
                )
            },
            eveningAthkar = evening.map { item ->
                val current = counts[item.id] ?: 0
                val freeCount = item.repeatCount <= 0
                AthkarItemState(
                    item = item,
                    currentCount = current,
                    isFreeCount = freeCount,
                    isCompleted = !freeCount && current >= item.repeatCount
                )
            },
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AthkarUiState(isLoading = true)
    )

    init {
        loadSavedCounts()
        loadData()
    }

    private fun loadSavedCounts() {
        val saved = dataStorePreference?.getDailyAthkarCounts(todayDateStr) ?: emptyMap()
        _countsMap.value = saved
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            getDuaItemListUseCase.invoke(27).collect { result ->
                if (result is Result.Success) {
                    _morningList.value = result.data
                }
            }
        }
        viewModelScope.launch {
            getDuaItemListUseCase.invoke(28).collect { result ->
                if (result is Result.Success) {
                    _eveningList.value = result.data
                }
                _isLoading.value = false
            }
        }
    }

    fun incrementCount(itemId: Int, maxCount: Int) {
        _countsMap.update { currentMap ->
            val existing = currentMap[itemId] ?: 0
            val updatedCount = existing + 1
            val newMap = currentMap + (itemId to updatedCount)
            dataStorePreference?.saveDailyAthkarCounts(todayDateStr, newMap)
            newMap
        }
    }

    fun resetItemCount(itemId: Int) {
        _countsMap.update { currentMap ->
            val newMap = currentMap + (itemId to 0)
            dataStorePreference?.saveDailyAthkarCounts(todayDateStr, newMap)
            newMap
        }
    }

    fun resetCounts(categoryId: Int) {
        val targetList = if (categoryId == 27) _morningList.value else _eveningList.value
        _countsMap.update { currentMap ->
            val newMap = currentMap.toMutableMap()
            targetList.forEach { item ->
                newMap[item.id] = 0
            }
            dataStorePreference?.saveDailyAthkarCounts(todayDateStr, newMap)
            newMap
        }
    }
}