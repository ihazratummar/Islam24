package com.hazrat.prayer.ui.setting

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.notification.PrayerRescheduleWorker
import com.hazrat.usecase.prayer.RefreshPrayerTimeUseCase
import com.hazrat.usecase.prayer.UpdateCalculationMethodUseCase
import com.hazrat.usecase.prayer.UpdateJuristicMethodUseCase
import com.hazrat.usecase.prayer.UserPrayerSettingUseCase
import com.hazrat.utils.network.ConnectivityObserver
import com.hazrat.utils.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PrayerSettingViewModel (
    private val application: Application,
    private val connectivityObserver: ConnectivityObserver,
    private val updateJuristicMethodUseCase: UpdateJuristicMethodUseCase,
    private val updateCalculationMethodUseCase: UpdateCalculationMethodUseCase,
    private val refreshPrayerTimeUseCase: RefreshPrayerTimeUseCase,
    private val userPrayerSettingUseCase: UserPrayerSettingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PrayerSettingState())
    val state = combine(
        _state,
        userPrayerSettingUseCase.invoke(),
    ){state, prayerSettingModel ->
        state.copy(
            userPrayerSettingModel = prayerSettingModel
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
                        val result = updateCalculationMethodUseCase.invoke(method = event.value)
                        when(result){
                            is Result.Error -> {
                                _state.update { it.copy(isRefresh = false) }
                            }
                            is Result.Success -> {
                                refreshPrayerTimeUseCase.invoke()
                                _state.update { it.copy(isRefresh = false) }
                            }
                        }
                        PrayerRescheduleWorker.enqueue(application)
                    } else {
                        _state.update { it.copy(isRefresh = false) }
                    }
                }
            }

            is PrayerSettingEvent.JuristicChanged -> {
                viewModelScope.launch {
                    _state.update { it.copy(isRefresh = true) }
                    val networkStatus = connectivityObserver.observer().first()
                    if (networkStatus == ConnectivityObserver.Status.Available) {

                        val result = updateJuristicMethodUseCase.invoke(method = event.value)
                        when(result){
                            is Result.Success -> {
                                refreshPrayerTimeUseCase.invoke()
                                _state.update { it.copy(isRefresh = false) }
                            }
                            is Result.Error -> {
                                _state.update { it.copy(isRefresh = false) }
                            }
                        }
                        _state.update { it.copy(isRefresh = false) }
                        PrayerRescheduleWorker.enqueue(application)
                    } else {
                        _state.update { it.copy(isRefresh = false) }
                    }
                }
            }
        }
    }
}
