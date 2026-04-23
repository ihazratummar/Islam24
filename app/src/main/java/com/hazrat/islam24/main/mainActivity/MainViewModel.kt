package com.hazrat.islam24.main.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.AppDataStore
import com.hazrat.location.repository.LocationRepository
import com.hazrat.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainViewModel(
    profileRepository: ProfileRepository,
    private val locationRepository: LocationRepository,
    private val appDataStore: AppDataStore
) : ViewModel() {


    val isDarkMode: StateFlow<Boolean>
    val isHapticFeedback: StateFlow<Boolean>


    init {
        viewModelScope.launch {
            profileRepository.checkAuthStatus()
        }

        viewModelScope.launch {
            locationRepository.getLastKnownLocation()
        }


        val initialDarkMode = runBlocking { appDataStore.getDarkModeEnabled() }
        isDarkMode = appDataStore.isDarkModeEnabled.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialDarkMode
        )
        val initialHaptic = runBlocking { appDataStore.getHapticEnabled() }
        isHapticFeedback = appDataStore.isHapticEnabled.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialHaptic
        )
    }

}