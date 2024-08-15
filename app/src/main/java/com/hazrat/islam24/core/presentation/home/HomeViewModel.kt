package com.hazrat.islam24.core.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val locationNameRepository: LocationNameRepository,
):ViewModel() {
    val locationName: StateFlow<List<LocationDetailsEntity>> = locationNameRepository.locationName
    val prayerTimes: StateFlow<List<PrayerTimeEntity>> = prayerTimeRepository.prayerTimes

    init {
        viewModelScope.launch {
            locationNameRepository.locationName()
            prayerTimeRepository.getAllPrayerTimes()
        }
    }
}