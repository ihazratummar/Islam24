//PrayerTimeViewmodl.kt

package com.hazrat.islam24.presentation.prayertime


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.data.prayertime.PrayerTimeEntity
import com.hazrat.islam24.data.location.locationdetails.LocationDetailsEntity
import com.hazrat.islam24.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.domain.repository.location.LocationNameRepository
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
class PrayerTimeViewModel @Inject constructor(
    private val repository: PrayerTimeRepository,
    private val locationNameRepository: LocationNameRepository
) : ViewModel() {

    private val _prayerTimes = MutableStateFlow<List<PrayerTimeEntity>>(emptyList())
    val prayerTimes = _prayerTimes.asStateFlow()


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

//    private val _locationName = MutableStateFlow<List<LocationDetailsEntity>>(emptyList())
//    val locationName = _locationName.asStateFlow()


    private fun getAllPrayerTimes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPrayer().distinctUntilChanged()
                .collectLatest { prayerList: List<PrayerTimeEntity> ->
                    if (prayerList.isEmpty()){
                        Log.d("testing",": Empty list ")
                    }else{
                        _prayerTimes.value = prayerList
                    }
                }
        }
    }

//    private fun locationNAme(){
//        viewModelScope.launch(Dispatchers.IO) {
//            locationNameRepository.getLocationDetails().distinctUntilChanged()
//                .collectLatest { locationName : List<LocationDetailsEntity> ->
//                    if (locationName.isEmpty()){
//                        Log.d("LocationNameStatus" , "Location list empty")
//                    }else{
//                        _locationName.value = locationName
//                    }
//                }
//        }
//    }

    init {
        viewModelScope.launch {
            getAllPrayerTimes()
//            locationNAme()
            repository.fetchAndSavePrayerTimesForMonth()
//            locationNameRepository.fetchLocationName()
            Log.d("GettingSomething", "${prayerTimes.value.size}")
        }

    }
}