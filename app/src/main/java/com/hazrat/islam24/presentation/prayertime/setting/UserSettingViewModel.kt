package com.hazrat.islam24.presentation.prayertime.setting

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.data.prayertime.PrayerSettingEntity
import com.hazrat.islam24.domain.model.prayertime.prayersettingmodel.MethodDetails
import com.hazrat.islam24.domain.model.prayertime.prayersettingmodel.SchoolDetails
import com.hazrat.islam24.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.domain.repository.prayertime.PrayerTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingViewModel @Inject constructor(
    private val repository: PrayerSettingRepository,
    private val prayerTimeRepository: PrayerTimeRepository
) : ViewModel() {

    private val _methodList = MutableStateFlow<List<PrayerSettingEntity>>(emptyList())
    val methodList = _methodList.asStateFlow()

    private val _methodDetails = MutableStateFlow<List<MethodDetails>>(emptyList())
    val methodDetails = _methodDetails.asStateFlow()

    var showMethodSelectionDialog by mutableStateOf(false)
    var showSchoolSelectionDialog by  mutableStateOf(false)
    var selectedMethod by mutableStateOf(MethodDetails(1, ""))
    var selectedSchool by mutableStateOf(SchoolDetails(0, ""))
    fun openMethodSelectionDialog() {
        showMethodSelectionDialog = true
    }
    fun openSchoolSelectionDialog() {
        showSchoolSelectionDialog = true
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMethod().distinctUntilChanged()
                .collect { listOfMethod ->
                    if (listOfMethod.isEmpty()) {
                        Log.d("MethodChecking", "Empty List")
                    } else {
                        _methodList.value = listOfMethod
                    }
                }
        }
    }

    fun insertMethod(prayerSettingEntity: PrayerSettingEntity) = viewModelScope.launch {
        repository.deleteAllMethod()
        repository.insertMethod(prayerSettingEntity)
        prayerTimeRepository.fetchAndSavePrayerTimesForMonth()

    }

    fun insertMethod(method: Int, school: Int?) {
        val prayerSettingEntity = PrayerSettingEntity(method = method, school = school)
        insertMethod(prayerSettingEntity)
    }
    fun updateMethod(prayerSettingEntity: PrayerSettingEntity) = viewModelScope.launch { repository.updateMethod(prayerSettingEntity) }

    fun deleteMethod(prayerSettingEntity: PrayerSettingEntity) = viewModelScope.launch { repository.deleteMethod(prayerSettingEntity) }
    fun deleteAllMethod() = viewModelScope.launch { repository.deleteAllMethod() }

    fun selectMethod(prayerSettingEntity: PrayerSettingEntity) {
        // Update the repository with the selected method
        insertMethod(prayerSettingEntity)
    }

    suspend fun deleteAllPrayer() = viewModelScope.launch { prayerTimeRepository.deleteAllPrayer() }


}