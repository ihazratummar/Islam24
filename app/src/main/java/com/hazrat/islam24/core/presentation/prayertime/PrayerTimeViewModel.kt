//PrayerTimeViewmodl.kt

package com.hazrat.islam24.core.presentation.prayertime


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PrayerTimeViewModel @Inject constructor(
    private val repository: PrayerTimeRepository,
) : ViewModel() {


    val prayerTimes: StateFlow<List<PrayerTimeEntity>> = repository.prayerTimes

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error


    fun onEvent(prayerEvent: PrayerEvent){
        when(prayerEvent){
            PrayerEvent.SharePrayer -> {
                repository.sharePrayerTimes(prayerTimes.value)
            }
        }
    }


    init {
        viewModelScope.launch {
            repository.getAllPrayerTimes()
        }
    }
}