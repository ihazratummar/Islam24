package com.hazrat.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.usecase.prayer.GetDailyPrayerStatusUseCase
import com.hazrat.usecase.GetIslamicEventsUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetNextFridayTime
import com.hazrat.usecase.prayer.GetTodayPrayerTimeUseCase
import com.hazrat.usecase.GetUpcomingMainIslamicEventUseCase
import com.hazrat.utils.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate

/**
 * @author Hazrat Ummar Shaikh
 */


class HomeViewModel(
    private val getTodayPrayerTimeUseCase: GetTodayPrayerTimeUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getUpcomingMainIslamicEventUseCase: GetUpcomingMainIslamicEventUseCase,
    private val islamicEventsUseCase: GetIslamicEventsUseCase,
    private val getNextFridayTime: GetNextFridayTime,
    private val dailyPrayerStatusUseCase: GetDailyPrayerStatusUseCase,
    private val clock: Clock = Clock.systemDefaultZone(),
) : ViewModel() {

    val today = LocalDate.now(clock)
    private val _locationName = MutableStateFlow(LocationName())
    val locationName = _locationName.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    val dailyStatus: StateFlow<DailyPrayerStatus?> = dailyPrayerStatusUseCase.invoke(today).stateIn(
        viewModelScope, started = SharingStarted.WhileSubscribed(5000) , null
    )


    init {
        refreshLocation()
        getTodayPrayer()
        loadMainIslamicEvent()
        loadIslamicEvents()

        viewModelScope.launch {
            getNextFridayTime.invoke().collectLatest { time ->
                _homeState.update {
                    it.copy(
                        fridayTime = time
                    )
                }
            }
        }
    }


    private fun loadMainIslamicEvent() {
        val event = getUpcomingMainIslamicEventUseCase.invoke()
        _homeState.update {
            it.copy(
                upcomingIslamicEvent = event
            )
        }
    }

    fun refreshLocation() {
        _homeState.update { it.copy(isLocationLoading = true) }
        viewModelScope.launch (Dispatchers.IO){
            getLocationNameUseCase.invoke().collectLatest { locationName ->
                _locationName.value = LocationName(address = locationName.locationName)
                _homeState.update { it.copy(isLocationLoading = false) }
            }
        }
    }

    private fun loadIslamicEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            islamicEventsUseCase.invoke().collectLatest { events ->
                _homeState.update {
                    it.copy(
                        islamicEventsInfoModel = events
                    )
                }
                Log.d("HomeViewModel", "List $events")
            }
        }
    }



    private fun getTodayPrayer() {
        viewModelScope.launch(Dispatchers.IO) {
            getTodayPrayerTimeUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Result.Error -> {

                    }
                    is Result.Success -> {
                        _homeState.update {
                            it.copy(
                                prayerData = result.data
                            )
                        }
                        Log.d("HomeViewModel", "Minimal Prayer Data ${result.data}")
                    }
                }
            }
        }
    }
}