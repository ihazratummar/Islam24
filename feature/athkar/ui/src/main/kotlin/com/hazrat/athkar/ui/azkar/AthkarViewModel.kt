package com.hazrat.athkar.ui.azkar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.athkar.domain.model.AthkarData
import com.hazrat.athkar.domain.repository.AthkarRepository
import com.hazrat.utils.network.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */


class AthkarViewModel (
    private val athkarRepository: AthkarRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _athkarList = MutableStateFlow<List<AthkarData>>(emptyList())
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