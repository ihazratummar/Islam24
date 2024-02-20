package com.hazrat.islam24.presentation.tasbih

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.data.entity.TasbihCounterEntity
import com.hazrat.islam24.domain.model.tasbihPhraseList
import com.hazrat.islam24.domain.repository.TasbihRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TasbihViewModel @Inject constructor(
    private val repository: TasbihRepository,
) : ViewModel() {

    val tasbihCounter: Flow<List<TasbihCounterEntity?>> = repository.getTasbih()

    fun insertTasbih(tasbihCounterEntity: TasbihCounterEntity) {
        viewModelScope.launch {
            repository.insertTasbih(tasbihCounterEntity)
        }
    }
    var selectedPhrase by mutableStateOf(tasbihPhraseList[0])

    fun resetTasbihCount(){
        viewModelScope.launch {
            repository.resetTasbihCount()
        }
    }
}

