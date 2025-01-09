//PrayerTimeViewmodl.kt

package com.hazrat.islam24.core.presentation.prayertime


import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.presentation.profiledetails.UserEvent
import com.hazrat.islam24.core.data.dao.PrayerTimeDao
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationState
import com.hazrat.islam24.notification.MediaPlayerHelper
import com.hazrat.islam24.notification.PrayerAlarmManager
import com.hazrat.islam24.util.Constants.PARENT_FOLDER_NAME_DOWNLOAD
import com.hazrat.islam24.util.Constants.SELECTED_ATHANS_SUB_FOLDER_NAME
import com.hazrat.islam24.util.datastore.DataStorePreference
import com.hazrat.islam24.util.DateUtil.getCurrentDate
import com.hazrat.islam24.util.MyFileUtils.saveMp3File
import com.hazrat.islam24.util.datastore.DataStore
import com.hazrat.islam24.util.datastore.NotificationType
import com.hazrat.islam24.util.datastore.PrayerName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class PrayerTimeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: PrayerTimeRepository,
    private val prayerAlarmManager: PrayerAlarmManager,
    private val dataStorePreference: DataStorePreference,
    private val prayerTimeDao: PrayerTimeDao,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val dataStore: DataStore
) : ViewModel() {


    private val eventChannel = Channel<UserEvent>()
    val events = eventChannel.receiveAsFlow()
    val prayerTimes: StateFlow<List<PrayerTimeEntity>> = repository.prayerTimes
    val prayerTimesByDate: StateFlow<List<PrayerTimeEntity>> = repository.prayerTimeByDate

    private val _notificationState = MutableStateFlow(
        NotificationState(
            isFajrNotification = dataStorePreference.getFajrNotification(),
            isDhuhrNotification = dataStorePreference.getDhuhrNotification(),
            isAsrNotification = dataStorePreference.getAsrNotification(),
            isMaghribNotification = dataStorePreference.getMaghribNotification(),
            isIshaNotification = dataStorePreference.getIshaNotification(),
        )
    )
    val notificationState = combine(
        _notificationState,
        dataStore.selectedFajrNotification.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )
    ) { state, selectedFajrNotification ->
        state.copy(selectedFajrAzan = selectedFajrNotification)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = _notificationState.value
    )

    fun onEvent(prayerEvent: PrayerEvent) {
        when (prayerEvent) {
            PrayerEvent.SharePrayer -> {
                repository.sharePrayerTimes(prayerTimes.value)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun onNotificationEvent(notificationEvent: NotificationEvent) {
        when (notificationEvent) {
            NotificationEvent.ToggleFajrNotification -> {
                if (checkExactAlarmPermission()) {
                    _notificationState.update {
                        it.copy(
                            isFajrNotification = !it.isFajrNotification
                        )
                    }
                    mediaPlayerHelper.releaseAzan()
                    viewModelScope.launch {
                        dataStorePreference.setFajrNotification(
                            _notificationState.value.isFajrNotification
                        )
                    }
                    if (_notificationState.value.isFajrNotification) {

                        val date = getCurrentDate()
                        val findDate = prayerTimes.value.find { it.gregorianDate == date }!!
                        val fajr = findDate.fajrTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            prayerAlarmManager.setFajrPrayerAlarm(fajr)
                        }
                    } else {
                        prayerAlarmManager.cancelFajrAlarm()
                    }
                } else {
                    openAppSettings()
                }
            }

            NotificationEvent.ToggleDhuhrNotification -> {
                if (checkExactAlarmPermission()) {
                    _notificationState.update {
                        it.copy(
                            isDhuhrNotification = !it.isDhuhrNotification
                        )
                    }
                    mediaPlayerHelper.releaseAzan()
                    viewModelScope.launch {
                        dataStorePreference.setDhuhrNotification(_notificationState.value.isDhuhrNotification)
                    }
                    if (_notificationState.value.isDhuhrNotification) {
                        val date = getCurrentDate()
                        val findDate = prayerTimes.value.find { it.gregorianDate == date }!!
                        val dhuhrTime = findDate.dhuhrTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            prayerAlarmManager.setDhuhrPrayerAlarm(dhuhrTime)
                        }
                    } else {
                        prayerAlarmManager.cancelDhuhrAlarm()
                    }
                } else {
                    openAppSettings()
                }
            }

            NotificationEvent.ToggleAsrNotification -> {
                if (checkExactAlarmPermission()) {

                    _notificationState.update {
                        it.copy(
                            isAsrNotification = !it.isAsrNotification
                        )
                    }
                    mediaPlayerHelper.releaseAzan()
                    viewModelScope.launch {
                        dataStorePreference.setAsrNotification(_notificationState.value.isAsrNotification)
                    }
                    if (_notificationState.value.isAsrNotification) {
                        val date = getCurrentDate()
                        val findDate = prayerTimes.value.find { it.gregorianDate == date }!!
                        val asrTime = findDate.asrTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            prayerAlarmManager.setAsrPrayerAlarm(asrTime)
                        }
                    } else {
                        prayerAlarmManager.cancelAsrAlarm()
                    }
                } else {
                    openAppSettings()
                }
            }

            NotificationEvent.ToggleMaghribNotification -> {
                if (checkExactAlarmPermission()) {

                    _notificationState.update {
                        it.copy(
                            isMaghribNotification = !it.isMaghribNotification
                        )
                    }
                    mediaPlayerHelper.releaseAzan()
                    viewModelScope.launch {
                        dataStorePreference.setMaghribNotification(_notificationState.value.isMaghribNotification)
                    }
                    if (_notificationState.value.isMaghribNotification) {
                        val date = getCurrentDate()
                        val findDate = prayerTimes.value.find { it.gregorianDate == date }!!
                        val maghribTime = findDate.maghribTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            prayerAlarmManager.setMaghribPrayerAlarm(maghribTime)
                        }
                    } else {
                        prayerAlarmManager.cancelMaghribAlarm()
                    }
                } else {
                    openAppSettings()
                }
            }

            NotificationEvent.ToggleIshaNotification -> {
                if (checkExactAlarmPermission()) {

                    _notificationState.update {
                        it.copy(
                            isIshaNotification = !it.isIshaNotification
                        )
                    }
                    mediaPlayerHelper.releaseAzan()
                    viewModelScope.launch {
                        dataStorePreference.setIshaNotification(_notificationState.value.isIshaNotification)
                    }
                    if (_notificationState.value.isIshaNotification) {
                        val date = getCurrentDate()
                        val findDate = prayerTimes.value.find { it.gregorianDate == date }!!
                        val ishaTime = findDate.ishaTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            prayerAlarmManager.setIshaPrayerAlarm(ishaTime)
                        }
                    } else {
                        prayerAlarmManager.cancelIshaAlarm()
                    }
                } else {
                    openAppSettings()
                }
            }

            NotificationEvent.RefreshNotificationState -> {
                viewModelScope.launch {
                    _notificationState.update {
                        it.copy(
                            isFajrNotification = dataStorePreference.getFajrNotification(),
                            isDhuhrNotification = dataStorePreference.getDhuhrNotification(),
                            isAsrNotification = dataStorePreference.getAsrNotification(),
                            isMaghribNotification = dataStorePreference.getMaghribNotification(),
                            isIshaNotification = dataStorePreference.getIshaNotification()
                        )
                    }
                }
            }

            is NotificationEvent.OnFajrAzanClick -> {
                viewModelScope.launch {
                    val success = saveMp3File(
                        context = context,
                        resourceInd = notificationEvent.resourceInd,
                        parentFolderName = PARENT_FOLDER_NAME_DOWNLOAD,
                        subFolderName = SELECTED_ATHANS_SUB_FOLDER_NAME,
                        fileName = "fajrAzan.mp3"
                    )
                    if (success) {
                        dataStore.savePrayerNotificationType(
                            prayerName = PrayerName.FAJR,
                            NotificationType.AZAN
                        )
                        Log.d(
                            "Notification",
                            "Aazn Fajr: ${notificationEvent.resourceInd}, NotificationType: ${NotificationType.AZAN}"
                        )
                    } else {
                        Log.e("PrayerTimeViewModel", "Failed to save MP3.")
                    }
                }
            }

            is NotificationEvent.OnDhuhrAzanClick -> {
                viewModelScope.launch {
                    val success = saveMp3File(
                        context = context,
                        resourceInd = notificationEvent.resourceInd,
                        parentFolderName = PARENT_FOLDER_NAME_DOWNLOAD,
                        subFolderName = SELECTED_ATHANS_SUB_FOLDER_NAME,
                        fileName = "dhurAzan.mp3"
                    )
                    if (success) {
                        dataStore.savePrayerNotificationType(
                            prayerName = PrayerName.DHUHR,
                            NotificationType.AZAN
                        )
                        Log.d(
                            "Notification",
                            "Aazn Dhuhr: ${notificationEvent.resourceInd}, NotificationType: ${NotificationType.AZAN}"
                        )
                    } else {
                        Log.e("PrayerTimeViewModel", "Failed to save MP3.")
                    }
                }
            }

            is NotificationEvent.OnAsrAzanClick -> {
                viewModelScope.launch {
                    val success = saveMp3File(
                        context = context,
                        resourceInd = notificationEvent.resourceInd,
                        parentFolderName = PARENT_FOLDER_NAME_DOWNLOAD,
                        subFolderName = SELECTED_ATHANS_SUB_FOLDER_NAME,
                        fileName = "asrAzan.mp3"
                    )
                    if (success) {
                        dataStore.savePrayerNotificationType(
                            prayerName = PrayerName.ASR,
                            notificationType = NotificationType.AZAN
                        )
                        Log.d(
                            "Notification",
                            "Aazn Asr: ${notificationEvent.resourceInd}, NotificationType: ${NotificationType.AZAN}"
                        )
                    } else {
                        Log.e("PrayerTimeViewModel", "Failed to save MP3.")
                    }
                }
            }

            is NotificationEvent.OnMaghribAzanClick -> {
                viewModelScope.launch {
                    val success = saveMp3File(
                        context = context,
                        resourceInd = notificationEvent.resourceInd,
                        parentFolderName = PARENT_FOLDER_NAME_DOWNLOAD,
                        subFolderName = SELECTED_ATHANS_SUB_FOLDER_NAME,
                        fileName = "maghribAzan.mp3"
                    )
                    if (success) {
                        dataStore.savePrayerNotificationType(
                            prayerName = PrayerName.MAGHRIB,
                            NotificationType.AZAN
                        )
                        Log.d(
                            "Notification",
                            "Aazn Maghrib: ${notificationEvent.resourceInd}, NotificationType: ${NotificationType.AZAN}"
                        )
                        Log.d("PrayerTimeViewModel", "NotificationEvent.OnFajrAzanClick: Success")
                    } else {
                        Log.e("PrayerTimeViewModel", "Failed to save MP3.")
                    }
                }
            }

            is NotificationEvent.OnIshaAzanClick -> {
                viewModelScope.launch {
                    val success = saveMp3File(
                        context = context,
                        resourceInd = notificationEvent.resourceInd,
                        parentFolderName = PARENT_FOLDER_NAME_DOWNLOAD,
                        subFolderName = SELECTED_ATHANS_SUB_FOLDER_NAME,
                        fileName = "ishaAzan.mp3"
                    )
                    if (success) {
                        dataStore.savePrayerNotificationType(
                            prayerName = PrayerName.ISHA,
                            notificationType = NotificationType.AZAN
                        )

                        Log.d(
                            "Notification",
                            "Aazn Isha: ${notificationEvent.resourceInd}, NotificationType: ${NotificationType.AZAN}"
                        )
                    } else {
                        Log.e("PrayerTimeViewModel", "Failed to save MP3.")
                    }
                }
            }

            is NotificationEvent.OnAzanPlayClick -> {
                _notificationState.update { state ->
                    state.copy(isAzanPlaying = state.isAzanPlaying.mapIndexed { index, isPlaying ->
                        index == notificationEvent.aazanIndex // Only the clicked Azan will be true
                    })
                }
                mediaPlayerHelper.playAzan(notificationEvent.resourceInd)
                mediaPlayerHelper.startAzan()
            }

            NotificationEvent.StopAzan -> {
                mediaPlayerHelper.stopAzan()
                mediaPlayerHelper.releaseAzan()
                _notificationState.update { it.copy(isAzanPlaying = it.isAzanPlaying.map { false }) }
            }

            is NotificationEvent.OnDefaultNotificationClick -> {
                viewModelScope.launch {
                    dataStore.savePrayerNotificationType(
                        prayerName = notificationEvent.azanName,
                        notificationType = notificationEvent.notificationType
                    )
                }
                Log.d(
                    "Notification",
                    "Default PrayerName: ${notificationEvent.azanName}, NotificationType: ${notificationEvent.notificationType}"
                )
            }

            is NotificationEvent.OnSilentNotificationClick -> {
                viewModelScope.launch {
                    dataStore.savePrayerNotificationType(
                        prayerName = notificationEvent.azanName,
                        notificationType = notificationEvent.notificationType
                    )
                }
                Log.d(
                    "Notification",
                    "Silent PrayerName: ${notificationEvent.azanName}, NotificationType: ${notificationEvent.notificationType}"
                )
            }

            is NotificationEvent.SelectFajrAzanOption -> {
                _notificationState.update {
                    it.copy(
                        selectedFajrAzan = notificationEvent.index
                    )
                }
                viewModelScope.launch {
                    dataStore.setSelectedFajrNotification(notificationEvent.index)
                }
            }

            is NotificationEvent.SelectDhuhrAzanOption -> {
                _notificationState.update {
                    it.copy(
                        selectedDhuhrAzan = notificationEvent.index
                    )
                }
                viewModelScope.launch {
                    dataStore.setSelectedDhuhrNotification(notificationEvent.index)
                }
            }

            is NotificationEvent.SelectAsrAzanOption -> {
                _notificationState.update {
                    it.copy(
                        selectedAsrAzan = notificationEvent.index
                    )
                }
                viewModelScope.launch {
                    dataStore.setSelectedAsrNotification(notificationEvent.index)
                }
            }

            is NotificationEvent.SelectMaghribAzanOption -> {
                _notificationState.update {
                    it.copy(
                        selectedMaghribAzan = notificationEvent.index
                    )
                }
                viewModelScope.launch {
                    dataStore.setSelectedMaghribNotification(notificationEvent.index)
                }
            }

            is NotificationEvent.SelectIshaAzanOption -> {
                _notificationState.update {
                    it.copy(
                        selectedIshaAzan = notificationEvent.index
                    )
                }
            }

        }
    }

    private fun checkExactAlarmPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            return alarmManager.canScheduleExactAlarms()
        }
        return true
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPrayerTimes()
            repository.getPrayerTimeByDate()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.parse("package:${context.packageName}")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

}
