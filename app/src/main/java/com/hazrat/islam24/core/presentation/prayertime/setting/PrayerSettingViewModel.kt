package com.hazrat.islam24.core.presentation.prayertime.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.PrayerCalculationEntity
import com.hazrat.islam24.core.data.entity.PrayerJuristicEntity
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerSettingViewModel @Inject constructor(
    private val repository: PrayerSettingRepository,
    private val prayerTimeRepository: PrayerTimeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PrayerSettingState())
    val state = _state.asStateFlow()

    private val _calculationMethod = MutableStateFlow<PrayerCalculationEntity?>(null)
    val calculationMethod = _calculationMethod.asStateFlow()

    private val _juristicMethod = MutableStateFlow<PrayerJuristicEntity?>(null)
    val juristicMethod = _juristicMethod.asStateFlow()

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
    init {
        getJuristicMethod()
        getCalculationMethod()
    }

    fun onEvent(event: PrayerSettingEvent) {
        when (event) {
            is PrayerSettingEvent.CalculationChanged -> {
                viewModelScope.launch {
                    repository.deleteCalculationMethod()
                    repository.insertCalculationMethod(
                        PrayerCalculationEntity(event.value)
                    )
                    prayerTimeRepository.fetchAndSavePrayerTimesForMonth()
                }
            }

            is PrayerSettingEvent.JuristicChanged -> {
                viewModelScope.launch {
                    repository.deleteJuristicMethod()
                    repository.insertJuristicMethod(
                        PrayerJuristicEntity(event.value)

                    )
                    prayerTimeRepository.fetchAndSavePrayerTimesForMonth()
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