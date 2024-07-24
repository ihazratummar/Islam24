package com.hazrat.islam24.core.presentation.namesofallah

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.NameEntity
import com.hazrat.islam24.core.domain.repository.NamesRepository
import com.hazrat.islam24.main.navigation.nvgraph.Route
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class NamesViewmodel @Inject constructor(
    private val namesRepository: NamesRepository,
    private val connectivityObserver: ConnectivityObserver,
): ViewModel() {

    /**
     * allah' names
     */
    private val _names = MutableStateFlow<List<NameEntity>>(emptyList())
    val names = _names.asStateFlow()

    /**
     * network check
     */
    private val _networkStatus = mutableStateOf(ConnectivityObserver.Status.Unavailable)
    val networkStatus: State<ConnectivityObserver.Status> = _networkStatus


    init {
        viewModelScope.launch {
            _names.value = namesRepository.getAllahNamesFromDatabase()
        }
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        connectivityObserver.observer().onEach { status ->
            _networkStatus.value = status
            if (status == ConnectivityObserver.Status.Available) {
                namesRepository.getAllahNamesFromApi()
            }
        }.launchIn(viewModelScope)
    }


}