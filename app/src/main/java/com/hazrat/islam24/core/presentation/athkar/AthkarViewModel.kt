package com.hazrat.islam24.core.presentation.athkar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.AthkarDataEntity
import com.hazrat.islam24.core.data.entity.NameEntity
import com.hazrat.islam24.core.domain.repository.AthkarRepository
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
* @author Hazrat Ummar Shaikh
*/

@HiltViewModel
class AthkarViewModel @Inject constructor(
    private val athkarRepository: AthkarRepository,
    private val networkRepository: NetworkRepository,
): ViewModel(){

    private val networkStatus: StateFlow<ConnectivityObserver.Status> =
        networkRepository.networkStatus

    private val _athkarList = MutableStateFlow<List<AthkarDataEntity>>(emptyList())
    val athkarList = _athkarList.asStateFlow()

    init {
        observeNetworkStatus()
        observeDataFromDatabase()
    }

    private fun observeDataFromDatabase() {
        viewModelScope.launch {
            _athkarList.value = athkarRepository.getAthkarFromDb()
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkStatus.collect { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    athkarRepository.getAthkarFromApi()
                }
            }
        }
    }
}