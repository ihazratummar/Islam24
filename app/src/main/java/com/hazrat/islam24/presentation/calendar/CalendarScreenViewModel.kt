package com.hazrat.islam24.presentation.calendar


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.data.entity.HijriCalendarEntity
import com.hazrat.islam24.domain.repository.GregorianToHijriRepository
import com.hazrat.islam24.domain.repository.HijriCalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CalendarScreenViewModel @Inject constructor(
    private val repository: GregorianToHijriRepository,
    private val hijriCalendarRepository: HijriCalendarRepository
) : ViewModel() {

    private val _hijriDate = MutableStateFlow<List<GregorianToHijriEntity>>(emptyList())
    val hijriDate = _hijriDate.asStateFlow()

    private val _hijriCalendar = MutableStateFlow<List<HijriCalendarEntity>>(emptyList())
    val hijriCalendar = _hijriCalendar.asStateFlow()

    private fun fetchHijriDate() {
        viewModelScope.launch {
            repository.gregorianToHijriEntity().distinctUntilChanged()
                .collectLatest { hijriDay: List<GregorianToHijriEntity> ->
                    if (hijriDay.isEmpty()) {
                        Log.d("testing", ": Empty list ")
                    } else {
                        _hijriDate.value = hijriDay
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
                        _hijriCalendar.value = calenderList
                        Log.d("testing", ": Empty list $calenderList")
                    }
                }
        }
    }

    init {
        viewModelScope.launch {
            repository.getGregorianToHijriDate()
            hijriCalendarRepository.getHijriCalendarFromApi()
        }
        fetchHijriDate()
        fetchHijriCalendar()
    }
}