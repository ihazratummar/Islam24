package com.hazrat.islam24.presentation.mainActivity

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.data.entity.HijriCalendarEntity
import com.hazrat.islam24.data.entity.TasbihCounterEntity
import com.hazrat.islam24.domain.model.namesofallah.Data
import com.hazrat.islam24.domain.model.tasbihPhraseList
import com.hazrat.islam24.domain.repository.GregorianToHijriRepository
import com.hazrat.islam24.domain.repository.HijriCalendarRepository
import com.hazrat.islam24.domain.repository.NamesRepository
import com.hazrat.islam24.domain.repository.TasbihRepository
import com.hazrat.islam24.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.presentation.nvgraph.Route
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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
     * network check
     */
    private val _networkStatus = mutableStateOf(ConnectivityObserver.Status.Unavailable)
    val networkStatus: State<ConnectivityObserver.Status> = _networkStatus

    /**
     * allah' names
     */
    private val _names = MutableLiveData<List<Data>>()
    val names: LiveData<List<Data>> get() = _names

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

    init {
        // Removed the readAppEntry logic since it's not needed anymore.
        _startDestination.value = Route.HomeNavigation.route
        viewModelScope.launch {
            delay(300)
            _splashCondition.value = false
            _names.value = namesRepository.getAllNames()
            gregorianToHijriRepository.getGregorianToHijriDate()
            hijriCalendarRepository.getHijriCalendarFromApi()
        }
        observeNetworkStatus()
        fetchHijriDate()
        fetchHijriCalendar()
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
            locationNameRepository.getLocationDetails()
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

    fun resetTasbihCount(){
        viewModelScope.launch {
            tasbihRepository.resetTasbihCount()
        }
    }

    /**
     * calendar function
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
    //--------/////
}