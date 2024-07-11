package com.hazrat.islam24.presentation.mainActivity

import android.icu.util.LocaleData
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.data.entity.HijriCalendarEntity
import com.hazrat.islam24.data.entity.LocationDetailsEntity
import com.hazrat.islam24.data.entity.NameEntity
import com.hazrat.islam24.data.entity.PrayerTimeEntity
import com.hazrat.islam24.data.entity.TasbihCounterEntity
import com.hazrat.islam24.domain.model.tasbihPhraseList
import com.hazrat.islam24.domain.repository.GregorianToHijriRepository
import com.hazrat.islam24.domain.repository.HijriCalendarRepository
import com.hazrat.islam24.domain.repository.NamesRepository
import com.hazrat.islam24.domain.repository.TasbihRepository
import com.hazrat.islam24.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.presentation.navigation.nvgraph.Route
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val locationNameRepository: LocationNameRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val namesRepository: NamesRepository,
    private val gregorianToHijriRepository: GregorianToHijriRepository,
    private val hijriCalendarRepository: HijriCalendarRepository,
    private val tasbihRepository: TasbihRepository,
) : ViewModel() {
    /**
     * splash screen condition
     */
    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    /**
     * app destination screen
     */
    private val _startDestination = mutableStateOf(Route.HomeNavigation.route)
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
     * tasbih
     */
    val tasbihCounter: Flow<List<TasbihCounterEntity?>> = tasbihRepository.getTasbih()
    var selectedPhrase by mutableStateOf(tasbihPhraseList[0])

    /**
     * Location name
     */
    private val _locationName = MutableStateFlow<List<LocationDetailsEntity>>(emptyList())
    val locationName = _locationName.asStateFlow()

    init {
        _startDestination.value = Route.HomeNavigation.route
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
            locationNAme()
            locationNameRepository.getLocationDetails()
            _names.value = namesRepository.getAllahNames()

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
            locationNameRepository.getLocationName()
            locationNameRepository.fetchLocationName()
            namesRepository.getAllNames()
            gregorianToHijriRepository.getGregorianToHijriDate()
            hijriCalendarRepository.getHijriCalendarFromApi()

        }
    }

    /**
     * tasbih
     */
    fun insertTasbih(tasbihCounterEntity: TasbihCounterEntity) {
        viewModelScope.launch {
            tasbihRepository.insertTasbih(tasbihCounterEntity)
        }
    }
    fun resetTasbihCount() {
        viewModelScope.launch {
            tasbihRepository.resetTasbihCount()
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

    fun getHijriDay(): String{
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
    private fun locationNAme() {
        viewModelScope.launch(Dispatchers.IO) {
            locationNameRepository.getLocationDetails().distinctUntilChanged()
                .collectLatest { locationName: List<LocationDetailsEntity> ->
                    if (locationName.isEmpty()) {
                        Log.d("LocationNameStatus", "Location list empty")
                    } else {
                        _locationName.value = locationName
                    }
                }
        }
    }
}