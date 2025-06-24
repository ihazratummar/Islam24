package com.hazrat.islam24.core.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.UserDataStore
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.utils.DateUtil.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
//    private val locationNameRepository: LocationNameRepository,
    private val dataStore: UserDataStore,
):ViewModel() {
//    val locationName: StateFlow<List<LocationDetailsEntity>> = locationNameRepository.locationName
    val prayerTimes: StateFlow<List<PrayerTimeEntity>> = prayerTimeRepository.prayerTimes

    private val _homeState = MutableStateFlow(HomeState())

    val homeState = _homeState.asStateFlow()



    init {
        viewModelScope.launch {
//            locationNameRepository.locationName()
            prayerTimeRepository.getAllPrayerTimes()
        }
        getRandomAyatNumber()
    }

    private fun getRandomAyatNumber() {
        val totalAyat = 6236
        viewModelScope.launch {
            // Get the stored date and random Ayat number
            val storedDate = dataStore.getDailyQuranDate.first()
            val storedRandomAyat = dataStore.getRandomAyatNumber.first()

            if (storedDate != getCurrentDate()) {
                // Generate a new random Ayat number if the date has changed
                val randomAyat = (1..totalAyat).random()
                dataStore.saveRandomAyatNumber(randomAyat)
                dataStore.saveDailyQuranDate(getCurrentDate())

                // Update state with the new random Ayat number
                _homeState.update {
                    it.copy(
                        dailyQuranDate = getCurrentDate(),
                        randomAyatNumber = randomAyat
                    )
                }
            } else {
                // Use the existing random Ayat number
                _homeState.update {
                    it.copy(
                        dailyQuranDate = storedDate,
                        randomAyatNumber = storedRandomAyat
                    )
                }
            }
        }
    }
}