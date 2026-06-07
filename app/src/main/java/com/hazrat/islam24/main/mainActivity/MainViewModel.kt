package com.hazrat.islam24.main.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.auth.domain.usecase.ObserveUserUseCase
import com.hazrat.datastore.AppDataStore
import com.hazrat.location.repository.LocationRepository
import com.hazrat.model.ReleaseNote
import com.hazrat.utils.ChangelogProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(
    private val observeUserUseCase: ObserveUserUseCase,
    private val locationRepository: LocationRepository,
    private val appDataStore: AppDataStore,
    private val context: android.content.Context
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean>
    val isHapticFeedback: StateFlow<Boolean>

    private val _showChangelog = MutableStateFlow<ReleaseNote?>(null)
    val showChangelog = _showChangelog.asStateFlow()

    init {
        viewModelScope.launch {
            observeUserUseCase().collect { _ ->
                // User state is being observed reactively
            }
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

        checkChangelog()
    }

    private fun checkChangelog() {
        viewModelScope.launch {
            val lastSeenVersion = appDataStore.getLastSeenVersionCode()
            val latestVersion = ChangelogProvider.getLatestVersionCode(context)

            if (latestVersion != 0 && latestVersion != lastSeenVersion) {
                _showChangelog.update { ChangelogProvider.getReleaseNotes(context).firstOrNull() }
            }
        }
    }

    fun onChangelogDismissed() {
        viewModelScope.launch {
            val latestVersion = ChangelogProvider.getLatestVersionCode(context)
            appDataStore.setLastSeenVersionCode(latestVersion)
            _showChangelog.update { null }
        }
    }
}
