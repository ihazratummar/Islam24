package com.hazrat.islam24.core.presentation.namesofallah

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.NameEntity
import com.hazrat.islam24.core.domain.repository.NamesRepository
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class NamesViewmodel @Inject constructor(
    private val namesRepository: NamesRepository,
    networkRepository: NetworkRepository,
) : ViewModel() {

    /**
     * allah' names
     */
    private val _names = MutableStateFlow<List<NameEntity>>(emptyList())
    val names = _names.asStateFlow()

    private val networkStatus: StateFlow<ConnectivityObserver.Status> =
        networkRepository.networkStatus

    init {
        observeNetworkStatus()
        observeNamesFromDatabase()
    }
    private fun observeNamesFromDatabase() {
        viewModelScope.launch {
            namesRepository.getAllahNamesFromDatabase()
                .distinctUntilChanged()
                .collectLatest { _names.value = it }
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkStatus.collect { status ->
                Log.d("NamesViewModel", "Network Status : $status")
                if (status == ConnectivityObserver.Status.Available) {
                    namesRepository.getAllahNamesFromApi()
                }
            }
        }
    }
}