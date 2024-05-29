package com.hazrat.islam24.presentation.mainActivity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.domain.usecases.app_entry.ReadAppEntry
import com.hazrat.islam24.presentation.nvgraph.Route
import com.hazrat.islam24.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val readAppEntry: ReadAppEntry,
    private val prayerTimeRepository: PrayerTimeRepository,
    private val locationNameRepository: LocationNameRepository,
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {

    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    private val _startDestination = mutableStateOf(Route.AppStartNavigation.route)
    val startDestination: State<String> = _startDestination

    private val _networkStatus = mutableStateOf(ConnectivityObserver.Status.Unavailable)
    val networkStatus : State<ConnectivityObserver.Status> = _networkStatus

    init {
        readAppEntry().onEach { shouldStartFromHomeScreen ->
            if (shouldStartFromHomeScreen){
                _startDestination.value = Route.HomeNavigation.route
            }else{
                _startDestination.value = Route.AppStartNavigation.route
            }
            delay(300)
            _splashCondition.value = false
        }.launchIn(viewModelScope)
        observeNetworkStatus()
    }

    private fun observeNetworkStatus(){
        connectivityObserver.observer().onEach { status ->
            _networkStatus.value = status
            if (status == ConnectivityObserver.Status.Available){
                fetchInitialData()
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            prayerTimeRepository.getAllPrayer()
            // prayerTimeRepository.fetchAndSavePrayerTimesForMonth()
            locationNameRepository.getLocationName()
            // locationNameRepository.getLocationDetails()
        }
    }
}