package com.hazrat.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.domain.repository.LocationNameRepository
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.usecase.GetTodayPrayerTimeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */


class HomeViewModel(
    private val locationNameRepository: LocationNameRepository,
    private val getTodayPrayerTimeUseCase: GetTodayPrayerTimeUseCase
) : ViewModel() {


    private val _locationName = MutableStateFlow(LocationName())
    val locationName = _locationName.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()


    init {
        refreshLocation()
        getTodayPrayer()
    }

    fun refreshLocation() {
        viewModelScope.launch {
            locationNameRepository.locationName().collectLatest { locationName ->
                _locationName.value = LocationName(address = locationName)
                Log.d("HomeViewModel", "Location name: $locationName")
            }
        }
    }

    private fun getTodayPrayer() {
        viewModelScope.launch(Dispatchers.IO) {
            getTodayPrayerTimeUseCase.invoke().collectLatest {prayerTimes ->
                _homeState.update {
                    it.copy(
                        prayerData = prayerTimes
                    )
                }
                Log.d("HomeViewModel", "Minimal Prayer Data $prayerTimes")
            }
        }
    }
}