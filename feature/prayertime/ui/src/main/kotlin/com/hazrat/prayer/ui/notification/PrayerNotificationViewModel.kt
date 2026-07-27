package com.hazrat.prayer.ui.notification

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.UserDataStore
import com.hazrat.model.Prayer
import com.hazrat.notification.PrayerAlarmScheduler
import com.hazrat.notification.PrayerRescheduleWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PrayerNotificationEvent {
    data class ToggleMaster(val enable: Boolean) : PrayerNotificationEvent
    data class TogglePrayer(val prayer: Prayer, val enabled: Boolean) : PrayerNotificationEvent
    data class ToggleVibration(val prayer: Prayer, val enabled: Boolean) : PrayerNotificationEvent
    data class SetPreAlertOffset(val prayer: Prayer, val minutes: Int) : PrayerNotificationEvent
    data class SetAzanSound(val prayer: Prayer, val soundName: String) : PrayerNotificationEvent
    data class ToggleAccordion(val prayer: Prayer) : PrayerNotificationEvent
    data class OpenPreAlertSheet(val prayer: Prayer) : PrayerNotificationEvent
    data object DismissPreAlertSheet : PrayerNotificationEvent
    data class OpenAzanSoundSheet(val prayer: Prayer) : PrayerNotificationEvent
    data object DismissAzanSoundSheet : PrayerNotificationEvent
}

class PrayerNotificationViewModel(
    private val application: Application,
    private val userDataStore: UserDataStore,
    private val prayerAlarmScheduler: PrayerAlarmScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerNotificationState())

    val uiState: StateFlow<PrayerNotificationState> = combine(
        _uiState,
        userDataStore.notificationSettingsFlow
    ) { state, notificationSettings ->
        val enabledMap = mapOf(
            Prayer.FAJR to notificationSettings.fajr,
            Prayer.DHUHR to notificationSettings.dhuhr,
            Prayer.ASR to notificationSettings.asr,
            Prayer.MAGHRIB to notificationSettings.maghrib,
            Prayer.ISHA to notificationSettings.isha
        )
        state.copy(enabledPrayers = enabledMap)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PrayerNotificationState()
    )

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            Prayer.entries.forEach { prayer ->
                launch {
                    userDataStore.getPrayerPreAlertOffset(prayer).collect { offset ->
                        _uiState.update { s ->
                            s.copy(preAlertOffsets = s.preAlertOffsets + (prayer to offset))
                        }
                    }
                }
                launch {
                    userDataStore.getPrayerVibrationEnabled(prayer).collect { vibration ->
                        _uiState.update { s ->
                            s.copy(vibrationStates = s.vibrationStates + (prayer to vibration))
                        }
                    }
                }
                launch {
                    userDataStore.getPrayerAzanSound(prayer).collect { sound ->
                        _uiState.update { s ->
                            s.copy(azanSounds = s.azanSounds + (prayer to sound))
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: PrayerNotificationEvent) {
        when (event) {
            is PrayerNotificationEvent.ToggleMaster -> {
                viewModelScope.launch {
                    Prayer.entries.forEach { prayer ->
                        userDataStore.setPrayerNotificationEnabled(prayer, event.enable)
                    }
                    triggerDynamicAlarmReschedule()
                }
            }

            is PrayerNotificationEvent.TogglePrayer -> {
                viewModelScope.launch {
                    userDataStore.setPrayerNotificationEnabled(event.prayer, event.enabled)
                    if (!event.enabled) {
                        // Collapse accordion if notification turned off!
                        _uiState.update { s ->
                            s.copy(expandedPrayers = s.expandedPrayers - event.prayer)
                        }
                    }
                    triggerDynamicAlarmReschedule()
                }
            }

            is PrayerNotificationEvent.ToggleVibration -> {
                viewModelScope.launch {
                    userDataStore.setPrayerVibrationEnabled(event.prayer, event.enabled)
                    _uiState.update { s ->
                        s.copy(vibrationStates = s.vibrationStates + (event.prayer to event.enabled))
                    }
                }
            }

            is PrayerNotificationEvent.SetPreAlertOffset -> {
                viewModelScope.launch {
                    userDataStore.setPrayerPreAlertOffset(event.prayer, event.minutes)
                    _uiState.update { s ->
                        s.copy(preAlertOffsets = s.preAlertOffsets + (event.prayer to event.minutes))
                    }
                    triggerDynamicAlarmReschedule()
                }
            }

            is PrayerNotificationEvent.SetAzanSound -> {
                viewModelScope.launch {
                    userDataStore.setPrayerAzanSound(event.prayer, event.soundName)
                    _uiState.update { s ->
                        s.copy(azanSounds = s.azanSounds + (event.prayer to event.soundName))
                    }
                }
            }

            is PrayerNotificationEvent.ToggleAccordion -> {
                _uiState.update { s ->
                    val isCurrentlyExpanded = s.expandedPrayers.contains(event.prayer)
                    val newExpanded = if (isCurrentlyExpanded) {
                        s.expandedPrayers - event.prayer
                    } else {
                        s.expandedPrayers + event.prayer
                    }
                    s.copy(expandedPrayers = newExpanded)
                }
            }

            is PrayerNotificationEvent.OpenPreAlertSheet -> {
                _uiState.update { s -> s.copy(activePreAlertSheetPrayer = event.prayer) }
            }

            PrayerNotificationEvent.DismissPreAlertSheet -> {
                _uiState.update { s -> s.copy(activePreAlertSheetPrayer = null) }
            }

            is PrayerNotificationEvent.OpenAzanSoundSheet -> {
                _uiState.update { s -> s.copy(activeAzanSoundSheetPrayer = event.prayer) }
            }

            PrayerNotificationEvent.DismissAzanSoundSheet -> {
                _uiState.update { s -> s.copy(activeAzanSoundSheetPrayer = null) }
            }
        }
    }

    private fun triggerDynamicAlarmReschedule() {
        PrayerRescheduleWorker.enqueue(application)
    }
}
