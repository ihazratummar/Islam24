package com.hazrat.prayer.ui.setting

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.DataStorePreference
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.model.PrayerTimeModel
import com.hazrat.notification.PrayerAlarmManager
import com.hazrat.utils.DateUtil.getCurrentDate
import com.hazrat.utils.network.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PrayerSettingViewModel (
    private val context: Context,
    private val prayerTimeRepository: PrayerTimeRepository,
    private val prayerAlarmManager: PrayerAlarmManager,
    private val dataStorePreference: DataStorePreference,
    private val connectivityObserver: ConnectivityObserver,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(PrayerSettingState())
    val state = combine(
        _state,
        userDataStore.getPrayerCalculationMethod,
        userDataStore.getPrayerJuristicMethod
    ){state, calculationMethod, juristicMethod ->
        state.copy(
            juristic = juristicMethod,
            calculationMethod = calculationMethod
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PrayerSettingState()
    )

    val prayerTime: StateFlow<List<PrayerTimeModel>> = prayerTimeRepository.prayerTimes


    fun onEvent(event: PrayerSettingEvent) {
        when (event) {
            is PrayerSettingEvent.CalculationChanged -> {
                viewModelScope.launch {
                    _state.update { it.copy(isRefresh = true) }
                    val networkStatus = connectivityObserver.observer().first()
                    if (networkStatus == ConnectivityObserver.Status.Available) {
                        val result = userDataStore.saveSetPrayerCalculationMethod(calculationMethod = event.value)
                        if (result){
                            prayerTimeRepository.newPrayerTimesRequest()
                        }
                        reScheduleAlarm()
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    _state.update { it.copy(isRefresh = false) }
                }
            }

            is PrayerSettingEvent.JuristicChanged -> {
                viewModelScope.launch {
                    _state.update { it.copy(isRefresh = true) }
                    val networkStatus = connectivityObserver.observer().first()
                    if (networkStatus == ConnectivityObserver.Status.Available) {

                        val result = userDataStore.savePrayerJuristicMethod(method = event.value)
                        if (result){
                            prayerTimeRepository.newPrayerTimesRequest()
                        }
                        reScheduleAlarm()
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    _state.update { it.copy(isRefresh = false) }
                }
            }

            PrayerSettingEvent.OpenCalculationDialog -> {
                _state.update {
                    it.copy(
                        isCalculationDialogOpen = !it.isCalculationDialogOpen
                    )
                }
            }

            PrayerSettingEvent.OpenJuristicDialog -> {
                _state.update {
                    it.copy(
                        isJuristicDialogOpen = !it.isJuristicDialogOpen
                    )
                }
            }
        }
    }

    private fun reScheduleAlarm() {

        if (!requestScheduleExactAlarmPermission(context = context)){
            Log.d("PrayerSettingViewModel", "Exact Alarm Permission Not Granted")
            return
        }

        val today = getCurrentDate()
        val getPrayer = prayerTime.value.find { it.gregorianDate == today }!!
        if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_FAJR_NOTIFICATION)) {
            prayerAlarmManager.setFajrPrayerAlarm(getPrayer.fajrTime)
        }
        if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_DHUHR_NOTIFICATION)) {
            prayerAlarmManager.setDhuhrPrayerAlarm(getPrayer.dhuhrTime)
        }
        if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_ASR_NOTIFICATION)) {
            prayerAlarmManager.setAsrPrayerAlarm(getPrayer.asrTime)
        }
        if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_MAGHRIB_NOTIFICATION)) {
            prayerAlarmManager.setMaghribPrayerAlarm(getPrayer.maghribTime)
        }
        if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_ISHA_NOTIFICATION)) {
            prayerAlarmManager.setIshaPrayerAlarm(getPrayer.ishaTime)
        }
    }

    fun requestScheduleExactAlarmPermission(context: Context) : Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        }else{
            true
        }
    }
}