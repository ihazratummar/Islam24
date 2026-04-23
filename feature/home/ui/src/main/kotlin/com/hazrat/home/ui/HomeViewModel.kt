package com.hazrat.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.LocationNameRepository
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.model.PrayerTimeModel
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.utils.DateUtil.getCurrentDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */



class HomeViewModel (
    private val prayerTimeRepository: PrayerTimeRepository,
    private val dataStore: UserDataStore,
    private val locationNameRepository: LocationNameRepository
) : ViewModel() {

    val prayerTimes: StateFlow<List<PrayerTimeModel>> = prayerTimeRepository.prayerTimes

    private val _locationName = MutableStateFlow(LocationName())
    val locationName = _locationName.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()


    init {
        viewModelScope.launch {
            prayerTimeRepository.getAllPrayerTimes()
        }
//        getRandomAyatNumber()
        refreshLocation()
    }

    fun refreshLocation(){
        viewModelScope.launch {
            locationNameRepository.locationName().collectLatest { locationName ->
                _locationName.value = LocationName(address = locationName)
                Log.d("HomeViewModel", "Location name: $locationName")
            }
        }
    }

//    private fun getRandomAyatNumber() {
//        val totalAyat = 6236
//        viewModelScope.launch {
//            // Get the stored date and random Ayat number
//            val storedDate = dataStore.getDailyQuranDate.first()
//            val storedRandomAyat = dataStore.getRandomAyatNumber.first()
//
//            if (storedDate != getCurrentDate()) {
//                // Generate a new random Ayat number if the date has changed
//                val randomAyat = (1..totalAyat).random()
//                dataStore.saveRandomAyatNumber(randomAyat)
//                dataStore.saveDailyQuranDate(getCurrentDate())
//
//                // Update state with the new random Ayat number
//                _homeState.update {
//                    it.copy(
//                        dailyQuranDate = getCurrentDate(),
//                        randomAyatNumber = randomAyat
//                    )
//                }
//            } else {
//                // Use the existing random Ayat number
//                _homeState.update {
//                    it.copy(
//                        dailyQuranDate = storedDate,
//                        randomAyatNumber = storedRandomAyat
//                    )
//                }
//            }
//        }
//    }
}