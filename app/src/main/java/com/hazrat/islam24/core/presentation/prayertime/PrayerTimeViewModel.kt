//PrayerTimeViewmodl.kt

package com.hazrat.islam24.core.presentation.prayertime


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
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
) : ViewModel() {


    private val _prayerTimes = MutableStateFlow<List<PrayerTimeEntity>>(emptyList())
    private val prayerTimes = _prayerTimes.asStateFlow()

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
            getAllPrayerTimes()
            repository.fetchAndSavePrayerTimesForMonth()
        }
    }

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

}