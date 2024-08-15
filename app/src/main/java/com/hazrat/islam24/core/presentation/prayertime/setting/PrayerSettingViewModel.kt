package com.hazrat.islam24.core.presentation.prayertime.setting

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.PrayerCalculationEntity
import com.hazrat.islam24.core.data.entity.PrayerJuristicEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerSettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: PrayerSettingRepository,
    private val prayerTimeRepository: PrayerTimeRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PrayerSettingState())
    val state = _state.asStateFlow()

    private val _calculationMethod = MutableStateFlow<PrayerCalculationEntity?>(null)
    val calculationMethod = _calculationMethod.asStateFlow()

    private val _juristicMethod = MutableStateFlow<PrayerJuristicEntity?>(null)
    val juristicMethod = _juristicMethod.asStateFlow()

    val prayerTime: StateFlow<List<PrayerTimeEntity>> = prayerTimeRepository.prayerTimes

    private val networkStatus: StateFlow<ConnectivityObserver.Status> = networkRepository.networkStatus

    init {
        getJuristicMethod()
        getCalculationMethod()
        viewModelScope.launch (Dispatchers.IO){
            prayerTimeRepository.getAllPrayerTimes()
            networkRepository.observeNetworkStatus()
        }
        Log.d("PrayerSettingViewModel", "Network status: ${networkStatus.value}")
    }

    private fun getCalculationMethod() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCalculationMethod().distinctUntilChanged()
                .collect {
                    _calculationMethod.value = it
                }
        }
    }
    private fun getJuristicMethod() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getJuristicMethod().distinctUntilChanged()
                .collect {
                    _juristicMethod.value = it
                }
        }
    }

    fun onEvent(event: PrayerSettingEvent) {
        when (event) {
            is PrayerSettingEvent.CalculationChanged -> {
                viewModelScope.launch {
                    if (networkStatus.value == ConnectivityObserver.Status.Available){
                        prayerTimeRepository.fetchAndSavePrayerTimesForMonth()
                        repository.insertCalculationMethod(
                            PrayerCalculationEntity(method = event.value)
                        )
                    }else{
                        Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            is PrayerSettingEvent.JuristicChanged -> {
                viewModelScope.launch {
                    if (networkStatus.value == ConnectivityObserver.Status.Available){
                        repository.insertJuristicMethod(
                            PrayerJuristicEntity(school = event.value)

                        )
                        prayerTimeRepository.fetchAndSavePrayerTimesForMonth()
                    }else{
                        Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show()
                    }
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