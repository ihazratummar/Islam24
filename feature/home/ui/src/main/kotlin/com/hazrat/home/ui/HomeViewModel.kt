package com.hazrat.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.usecase.GetIslamicEventsUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetTodayPrayerTimeUseCase
import com.hazrat.usecase.GetUpcomingIslamicEventUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Timer

/**
 * @author Hazrat Ummar Shaikh
 */


class HomeViewModel(
    private val getTodayPrayerTimeUseCase: GetTodayPrayerTimeUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getUpcomingIslamicEventUseCase: GetUpcomingIslamicEventUseCase,
    private val islamicEventsUseCase: GetIslamicEventsUseCase
) : ViewModel() {


    private val _locationName = MutableStateFlow(LocationName())
    val locationName = _locationName.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()


    init {
        refreshLocation()
        getTodayPrayer()
        loadRamadanEvent()
        loadIslamicEvents()
    }

    private fun loadRamadanEvent() {
        val event = getUpcomingIslamicEventUseCase.invoke()
        _homeState.update {
            it.copy(
                upcomingIslamicEvent = event
            )
        }
    }

    fun refreshLocation() {
        viewModelScope.launch {
            getLocationNameUseCase.invoke().collectLatest { locationName ->
                _locationName.value = LocationName(address = locationName)
                Log.d("HomeViewModel", "Location name: $locationName")
            }
        }
    }

    private fun loadIslamicEvents() {
        viewModelScope.launch (Dispatchers.IO){
            val events = islamicEventsUseCase.invoke()
            _homeState.update {
                it.copy(
                    islamicEventsInfoModel = events
                )
            }
            Log.d("HomeViewModel", "List $events")
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