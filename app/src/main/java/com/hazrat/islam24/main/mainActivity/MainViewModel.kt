package com.hazrat.islam24.main.mainActivity

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.core.domain.repository.location.LocationRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationNameRepository: LocationNameRepository,
    private val profileRepository: ProfileRepository,
    private val locationRepository: LocationRepository,
    private val networkRepository: NetworkRepository,
    @ApplicationContext context: Context
) : ViewModel() {



    /**
     * network check
     */

    val locationName: StateFlow<List<LocationDetailsEntity>> = locationNameRepository.locationName

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