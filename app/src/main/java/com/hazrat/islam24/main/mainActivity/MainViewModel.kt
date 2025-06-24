package com.hazrat.islam24.main.mainActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.AppDataStore
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.core.domain.repository.location.LocationRepository
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationNameRepository: LocationNameRepository,
    profileRepository: ProfileRepository,
    private val locationRepository: LocationRepository,
    networkRepository: NetworkRepository,
    private val appDataStore: AppDataStore
) : ViewModel() {



    /**
     * network check
     */

    val locationName: StateFlow<List<LocationDetailsEntity>> = locationNameRepository.locationName

    val isDarkMode : StateFlow<Boolean>
    val isHapticFeedback  : StateFlow<Boolean>


    private val networkStatus: StateFlow<ConnectivityObserver.Status> =
        networkRepository.networkStatus


    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchDataFromDB()
            networkStatus.collect { status ->
                Log.d("MainViewModel", "Network Status : $status")
                if (status == ConnectivityObserver.Status.Available) {
                    fetchInitialData()
                }
            }
        }
        profileRepository.checkAuthStatus()
        val initialDarkMode = runBlocking{ appDataStore.getDarkModeEnabled() }
        isDarkMode = appDataStore.isDarkModeEnabled.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialDarkMode
        )
        val initialHaptic = runBlocking { appDataStore.getHapticEnabled()}
        isHapticFeedback = appDataStore.isHapticEnabled.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialHaptic
        )
    }

    private fun fetchDataFromDB() {
        viewModelScope.launch {
            locationNameRepository.locationName()
            locationNameRepository.getLocationDetails()
        }
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            locationRepository.checkAndUpdateLocation()
            locationNameRepository.getLocationName()
            locationNameRepository.fetchLocationName()
        }
    }

}