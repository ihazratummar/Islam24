package com.hazrat.prayer.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.utils.network.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PrayerSettingViewModel (
    private val prayerTimeRepository: PrayerTimeRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(PrayerSettingState())
    val state = combine(
        _state,
        userDataStore.getPrayerCalculationMethod,
        userDataStore.getPrayerJuristicMethod
    ){state, calculationMethod, juristicMethod ->
        state.copy(
            juristic = juristicMethod,
            calculationMethod = calculationMethod
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PrayerSettingState()
    )


    fun onEvent(event: PrayerSettingEvent) {
        when (event) {
            is PrayerSettingEvent.CalculationChanged -> {
                viewModelScope.launch {
                    _state.update { it.copy(isRefresh = true) }
                    val networkStatus = connectivityObserver.observer().first()
                    if (networkStatus == ConnectivityObserver.Status.Available) {
                        val result = userDataStore.saveSetPrayerCalculationMethod(calculationMethod = event.value)
                        if (result){
                            prayerTimeRepository.refreshPrayerTimes()
                        }
//                        reScheduleAlarm() [PRAYER SETTING][TODO][HIGH] ADD RESCHEDULER
                    } else {

                    }
                    _state.update { it.copy(isRefresh = false) }
                }
            }

            is PrayerSettingEvent.JuristicChanged -> {
                viewModelScope.launch {
                    _state.update { it.copy(isRefresh = true) }
                    val networkStatus = connectivityObserver.observer().first()
                    if (networkStatus == ConnectivityObserver.Status.Available) {

                        val result = userDataStore.savePrayerJuristicMethod(method = event.value)
                        if (result){
                            prayerTimeRepository.refreshPrayerTimes()
                        }
//                        reScheduleAlarm()
                    } else {

                    }
                    _state.update { it.copy(isRefresh = false) }
                }
            }

            PrayerSettingEvent.OpenCalculationDialog -> {
                _state.update {
                    it.copy(
                        isCalculationDialogOpen = !it.isCalculationDialogOpen
                    )
                }
            }

            PrayerSettingEvent.OpenJuristicDialog -> {
                _state.update {
                    it.copy(
                        isJuristicDialogOpen = !it.isJuristicDialogOpen
                    )
                }
            }
        }
    }
}