package com.hazrat.islam24.main.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.AppDataStore
import com.hazrat.location.repository.LocationRepository
import com.hazrat.model.ReleaseNote
import com.hazrat.usecase.profile.IsLoggedInUseCase
import com.hazrat.usecase.profile.IsSubscribedUseCase
import com.hazrat.utils.ChangelogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(
    private val locationRepository: LocationRepository,
    private val appDataStore: AppDataStore,
    private val changelogRepository: ChangelogRepository,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val isSubscribedUseCase: IsSubscribedUseCase
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean>
    val isHapticFeedback: StateFlow<Boolean>

    /**
     * Null means initial state is still loading from DataStore/Prefs.
     * True/False represents resolved authentication state.
     */
    val isLoggedIn: StateFlow<Boolean?> = isLoggedInUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    /**
     * Null means initial state is still loading from DataStore/Prefs.
     * True/False represents resolved subscription state.
     */
    val isSubscribed: StateFlow<Boolean?> = isSubscribedUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val _showChangelog = MutableStateFlow<ReleaseNote?>(null)
    val showChangelog = _showChangelog.asStateFlow()

    init {
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

        checkChangelog()
    }

    private fun checkChangelog() {
        viewModelScope.launch {
            val lastSeenVersion = appDataStore.getLastSeenVersionCode()
            val latestVersion = changelogRepository.getLatestVersionCode()

            if (latestVersion != 0 && latestVersion != lastSeenVersion) {
                _showChangelog.update { changelogRepository.getReleaseNotes().firstOrNull() }
            }
        }
    }

    fun onChangelogDismissed() {
        viewModelScope.launch {
            val latestVersion = changelogRepository.getLatestVersionCode()
            appDataStore.setLastSeenVersionCode(latestVersion)
            _showChangelog.update { null }
        }
    }
}
