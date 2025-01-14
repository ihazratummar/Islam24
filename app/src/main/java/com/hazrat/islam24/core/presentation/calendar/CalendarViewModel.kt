package com.hazrat.islam24.core.presentation.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.core.data.entity.HijriCalendarEntity
import com.hazrat.islam24.core.domain.repository.GregorianToHijriRepository
import com.hazrat.islam24.core.domain.repository.HijriCalendarRepository
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */


@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val gregorianToHijriRepository: GregorianToHijriRepository,
    private val hijriCalendarRepository: HijriCalendarRepository,
    private val networkRepository: NetworkRepository,
): ViewModel() {


    /**
     * calenar
     */
    private val _gregorianToHijriEntity = MutableStateFlow<List<GregorianToHijriEntity>>(emptyList())
    val gregorianToHijriEntity = _gregorianToHijriEntity.asStateFlow()

    private val _hijriCalendarEntity = MutableStateFlow<List<HijriCalendarEntity>>(emptyList())
    val hijriCalendarEntity = _hijriCalendarEntity.asStateFlow()

    private val networkStatus: StateFlow<ConnectivityObserver.Status> =
        networkRepository.networkStatus

    init {
        observeNetworkStatus()
        fetchHijriDate()
        fetchHijriCalendar()
    }



    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkStatus.collect { status ->
                Log.d("NamesViewModel", "Network Status : $status")
                if (status == ConnectivityObserver.Status.Available) {
                    gregorianToHijriRepository.getGregorianToHijriDate()
                    hijriCalendarRepository.getHijriCalendarFromApi()
                }
            }
        }
    }

    /**
     * calendar function from db
     */
    private fun fetchHijriDate() {
        viewModelScope.launch {
            gregorianToHijriRepository.gregorianToHijriEntity().distinctUntilChanged()
                .collectLatest { hijriDay: List<GregorianToHijriEntity> ->
                    if (hijriDay.isEmpty()) {
                        Log.d("testing", ": Empty list ")
                    } else {
                        _gregorianToHijriEntity.value = hijriDay
                    }
                }
        }
    }

    private fun fetchHijriCalendar() {
        viewModelScope.launch {
            hijriCalendarRepository.getCalendarList().distinctUntilChanged()
                .collectLatest { calenderList: List<HijriCalendarEntity> ->
                    if (calenderList.isEmpty()) {
                        Log.d("testing", ": Empty list ")
                    } else {
                        _hijriCalendarEntity.value = calenderList
                        Log.d("testing", ": Data $calenderList")
                    }
                }
        }
    }
}