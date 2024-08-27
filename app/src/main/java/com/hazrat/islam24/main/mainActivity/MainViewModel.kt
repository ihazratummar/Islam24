package com.hazrat.islam24.main.mainActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.data.manager.NamesRepositoryImpl
import com.hazrat.islam24.core.domain.repository.GregorianToHijriRepository
import com.hazrat.islam24.core.domain.repository.HijriCalendarRepository
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.core.domain.repository.location.LocationRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val locationNameRepository: LocationNameRepository,
    private val namesRepository: NamesRepositoryImpl,
    private val gregorianToHijriRepository: GregorianToHijriRepository,
    private val hijriCalendarRepository: HijriCalendarRepository,
    private val profileRepository: ProfileRepository,
    private val locationRepository: LocationRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {


    /**
     * prayer time
     */
    private val _prayerTimes = MutableStateFlow<List<PrayerTimeEntity>>(emptyList())
    val prayerTimes: StateFlow<List<PrayerTimeEntity>> = _prayerTimes.asStateFlow()

    /**
     * network check
     */
    private val networkStatus: StateFlow<ConnectivityObserver.Status> =
        networkRepository.networkStatus


    val locationName: StateFlow<List<LocationDetailsEntity>> = locationNameRepository.locationName


    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchDataFromDB()
            networkRepository.observeNetworkStatus()
            profileRepository.networkObserver()
            prayerTimeRepository.networkObserver()
            networkStatus.collect { status ->
                Log.d("MainViewModelNetworkStatus", "Current network status: $status")
                if (status == ConnectivityObserver.Status.Available) {
                    Log.d("MainViewModelNetworkStatus", "Network available, fetching initial data.")
                    fetchInitialData()
                }
            }
        }
        profileRepository.checkAuthStatus()
        Log.d("mainViewModel", "Network status: ${networkStatus.value}")

    }

    private fun fetchDataFromDB() {
        viewModelScope.launch {
            getAllPrayerTimes()
            locationNameRepository.locationName()
            locationNameRepository.getLocationDetails()

        }
    }

    fun fetchData() {
        viewModelScope.launch {
            networkStatus.collect { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    locationRepository.checkAndUpdateLocation()
                    locationNameRepository.getLocationName()
                    locationNameRepository.fetchLocationName()
                }
            }
        }
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            prayerTimeRepository.getAllPrayer()
            prayerTimeRepository.fetchAndSavePrayerTimesForMonth()
            locationNameRepository.fetchLocationName()
            gregorianToHijriRepository.getGregorianToHijriDate()
            hijriCalendarRepository.getHijriCalendarFromApi()
            namesRepository.getAllahNamesFromApi()
            locationRepository.checkAndUpdateLocation()
            locationNameRepository.getLocationName()
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
}