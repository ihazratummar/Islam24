package com.hazrat.islam24.main.mainActivity

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.core.data.entity.HijriCalendarEntity
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.NameEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.data.manager.LocationNameRepositoryImpl
import com.hazrat.islam24.core.data.manager.NamesRepositoryImpl
import com.hazrat.islam24.core.domain.repository.GregorianToHijriRepository
import com.hazrat.islam24.core.domain.repository.HijriCalendarRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.main.navigation.nvgraph.Route
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val locationNameRepository: LocationNameRepositoryImpl,
    private val connectivityObserver: ConnectivityObserver,
    private val namesRepository: NamesRepositoryImpl,
    private val gregorianToHijriRepository: GregorianToHijriRepository,
    private val hijriCalendarRepository: HijriCalendarRepository,
) : ViewModel() {
    /**
     * splash screen condition
     */
    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    /**
     * app destination screen
     */
    private val _startDestination = mutableStateOf(Route.RootNav.route)
    val startDestination: State<String> = _startDestination

    /**
     * prayer time
     */
    private val _prayerTimes = MutableStateFlow<List<PrayerTimeEntity>>(emptyList())
    val prayerTimes = _prayerTimes.asStateFlow()

    /**
     * network check
     */
    private val _networkStatus = mutableStateOf(ConnectivityObserver.Status.Unavailable)
    val networkStatus: State<ConnectivityObserver.Status> = _networkStatus

    /**
     * allah' names
     */
    private val _names = MutableStateFlow<List<NameEntity>>(emptyList())
    val names = _names.asStateFlow()

    /**
     * calenar
     */
    private val _hijriDate = MutableStateFlow<List<GregorianToHijriEntity>>(emptyList())
    val hijriDate = _hijriDate.asStateFlow()

    private val _hijriCalendar = MutableStateFlow<List<HijriCalendarEntity>>(emptyList())
    val hijriCalendar = _hijriCalendar.asStateFlow()


    /**
     * Location name
     */
    private val _locationName = MutableStateFlow<List<LocationDetailsEntity>>(emptyList())
    val locationName = _locationName.asStateFlow()



    init {
        _startDestination.value = Route.RootNav.route
        viewModelScope.launch {
            delay(300)
            _splashCondition.value = false
            fetchDataFromDB()
        }
        observeNetworkStatus()

    }

    private fun fetchDataFromDB() {
        viewModelScope.launch {
            fetchHijriDate()
            fetchHijriCalendar()
            getAllPrayerTimes()
            locationName()
            locationNameRepository.getLocationDetails()
            _names.value = namesRepository.getAllahNamesFromDatabase()
        }
    }

    private fun observeNetworkStatus() {
        connectivityObserver.observer().onEach { status ->
            _networkStatus.value = status
            if (status == ConnectivityObserver.Status.Available) {
                fetchInitialData()
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            prayerTimeRepository.getAllPrayer()
            prayerTimeRepository.fetchAndSavePrayerTimesForMonth()
            locationNameRepository.fetchLocationName()
            gregorianToHijriRepository.getGregorianToHijriDate()
            hijriCalendarRepository.getHijriCalendarFromApi()
            namesRepository.getAllahNamesFromApi()
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

    fun getHijriDay(): String {
        val currentGregorianDay = getCurrentDay()

        val hijriDateEntity = _prayerTimes.value.find {
            it.day == currentGregorianDay
        }
        val hijriDay = hijriDateEntity?.hijriDay ?: "NO"
        Log.d("HijriDay", "Gregorian Date: $currentGregorianDay, Hijri Day: $hijriDay")
        return hijriDay
    }
    //--------/////

    /**
     *  Prayer Time from db
     */
    private fun getAllPrayerTimes() {
        viewModelScope.launch(Dispatchers.IO) {
            prayerTimeRepository.getAllPrayer().distinctUntilChanged()
                .collectLatest { prayerList: List<PrayerTimeEntity> ->
                    if (prayerList.isEmpty()) {
                        Log.d("testing", ": Empty list ")
                    } else {
                        _prayerTimes.value = prayerList
                    }
                }
        }
    }

    /**
     *  Location Name from db
     */
    private fun locationName() {
        viewModelScope.launch(Dispatchers.IO) {
            locationNameRepository.getLocationDetails().distinctUntilChanged()
                .collectLatest { locationName: List<LocationDetailsEntity> ->
                    if (locationName.isEmpty()) {
                        if (_networkStatus.value == ConnectivityObserver.Status.Available){
                            locationNameRepository.fetchLocationName()
                        }else{
                            return@collectLatest
                        }
                        Log.d("LocationNameStatus", "Location list empty")
                    } else {
                        _locationName.value = locationName
                    }
                }
        }
    }
}