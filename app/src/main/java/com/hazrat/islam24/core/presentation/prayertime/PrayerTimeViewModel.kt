//PrayerTimeViewmodl.kt

package com.hazrat.islam24.core.presentation.prayertime


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.presentation.UiText
import com.hazrat.islam24.auth.presentation.profiledetails.UserEvent
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationState
import com.hazrat.islam24.notification.PrayerAlarmManager
import com.hazrat.islam24.util.DataStorePreference
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PrayerTimeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: PrayerTimeRepository,
    private val prayerAlarmManager: PrayerAlarmManager,
    private val dataStorePreference: DataStorePreference
) : ViewModel() {


    private val eventChannel = Channel<UserEvent>()
    val events = eventChannel.receiveAsFlow()
    val prayerTimes: StateFlow<List<PrayerTimeEntity>> = repository.prayerTimes

    private val _notificationState = MutableStateFlow(
        NotificationState(
            isFajrNotification = dataStorePreference.getFajrNotification(),
            isDhuhrNotification = dataStorePreference.getDhuhrNotification(),
            isAsrNotification = dataStorePreference.getAsrNotification(),
            isMaghribNotification = dataStorePreference.getMaghribNotification(),
            isIshaNotification = dataStorePreference.getIshaNotification()
        )
    )
    val notificationState = _notificationState.asStateFlow()

    fun onEvent(prayerEvent: PrayerEvent) {
        when (prayerEvent) {
            PrayerEvent.SharePrayer -> {
                repository.sharePrayerTimes(prayerTimes.value)
            }
        }
    }

    fun onNotificationEvent(notificationEvent: NotificationEvent) {
        when (notificationEvent) {
            NotificationEvent.ToggleFajrNotification -> {
                _notificationState.update {
                    it.copy(
                        isFajrNotification = !it.isFajrNotification
                    )
                }
                viewModelScope.launch {
                    dataStorePreference.setFajrNotification(
                        _notificationState.value.isFajrNotification
                    )
                }
                if (_notificationState.value.isFajrNotification) {
                    val today = getCurrentDay()
                    if (ActivityCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        prayerAlarmManager.setFajrPrayerAlarm(prayerTimes.value[today - 1].fajrTime)
                    }
                } else {
                    prayerAlarmManager.cancelFajrAlarm()
                }
            }

            NotificationEvent.ToggleDhuhrNotification -> {
                _notificationState.update {
                    it.copy(
                        isDhuhrNotification = !it.isDhuhrNotification
                    )
                }
                viewModelScope.launch {
                    dataStorePreference.setDhuhrNotification(_notificationState.value.isDhuhrNotification)
                }
                if (_notificationState.value.isDhuhrNotification) {
                    val today = getCurrentDay()
                    if (ActivityCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        prayerAlarmManager.setDhuhrPrayerAlarm(prayerTimes.value[today - 1].dhuhrTime)
                    }
                } else {
                    prayerAlarmManager.cancelDhuhrAlarm()
                }
            }

            NotificationEvent.ToggleAsrNotification -> {
                _notificationState.update {
                    it.copy(
                        isAsrNotification = !it.isAsrNotification
                    )
                }
                viewModelScope.launch {
                    dataStorePreference.setAsrNotification(_notificationState.value.isAsrNotification)
                }
                if (_notificationState.value.isAsrNotification) {
                    val today = getCurrentDay()
                    if (ActivityCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        prayerAlarmManager.setAsrPrayerAlarm(prayerTimes.value[today - 1].asrTime)
                    }
                } else {
                    prayerAlarmManager.cancelAsrAlarm()
                }
            }
            NotificationEvent.ToggleMaghribNotification -> {
                _notificationState.update {
                    it.copy(
                        isMaghribNotification = !it.isMaghribNotification
                    )
                }
                viewModelScope.launch {
                    dataStorePreference.setMaghribNotification(_notificationState.value.isMaghribNotification)
                }
                if (_notificationState.value.isMaghribNotification) {
                    val today = getCurrentDay()
                    if (ActivityCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        prayerAlarmManager.setMaghribPrayerAlarm(prayerTimes.value[today - 1].maghribTime)
                    }
                } else {
                    prayerAlarmManager.cancelMaghribAlarm()
                }
            }
            NotificationEvent.ToggleIshaNotification -> {
                _notificationState.update {
                    it.copy(
                        isIshaNotification = !it.isIshaNotification
                    )
                }
                viewModelScope.launch {
                    dataStorePreference.setIshaNotification(_notificationState.value.isIshaNotification)
                }
                if (_notificationState.value.isIshaNotification) {
                    val today = getCurrentDay()
                    if (ActivityCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        prayerAlarmManager.setIshaPrayerAlarm(prayerTimes.value[today - 1].ishaTime)
                    }
                } else {
                    prayerAlarmManager.cancelIshaAlarm()
                }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPrayerTimes()
            repository.networkObserver()
        }
    }
}
