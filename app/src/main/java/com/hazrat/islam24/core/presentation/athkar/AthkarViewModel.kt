package com.hazrat.islam24.core.presentation.athkar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.AthkarDataEntity
import com.hazrat.islam24.core.domain.repository.AthkarRepository
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class AthkarViewModel @Inject constructor(
    private val athkarRepository: AthkarRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _athkarList = MutableStateFlow<List<AthkarDataEntity>>(emptyList())
    val athkarList = _athkarList.asStateFlow()

    init {
        getAthkarFromOrigin()
        observeDataFromDatabase()
    }

    private fun observeDataFromDatabase() {
        viewModelScope.launch {
            _athkarList.value = athkarRepository.getAthkarFromDb()
        }
    }

    private fun getAthkarFromOrigin() {
        viewModelScope.launch {
            val networkStatus = connectivityObserver.observer().first()
            if (networkStatus == ConnectivityObserver.Status.Available) {
                athkarRepository.getAthkarFromApi()
            }
        }
    }
}