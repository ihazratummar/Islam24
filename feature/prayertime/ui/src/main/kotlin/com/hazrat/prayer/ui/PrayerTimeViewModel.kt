//PrayerTimeViewmodl.kt

package com.hazrat.prayer.ui


import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.DataStorePreference
import com.hazrat.datastore.NotificationType
import com.hazrat.datastore.PrayerName
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.downloader.Downloader
import com.hazrat.downloader.MyFileUtils.saveMp3File
import com.hazrat.model.PrayerTimeModel
import com.hazrat.notification.MediaPlayerHelper
import com.hazrat.notification.PrayerAlarmManager
import com.hazrat.prayer.ui.notification.NotificationEvent
import com.hazrat.prayer.ui.notification.NotificationState
import com.hazrat.utils.Constants.DOWNLOADED_AZAN_FOLDER
import com.hazrat.utils.Constants.PARENT_FOLDER_NAME_DOWNLOAD
import com.hazrat.utils.Constants.SELECTED_ATHANS_SUB_FOLDER_NAME
import com.hazrat.utils.DateUtil.getCurrentDate
import com.hazrat.utils.MyFileUtils.isFilePresent
import com.hazrat.utils.network.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.system.measureTimeMillis


class PrayerTimeViewModel(
    private val context: Context,
    private val repository: PrayerTimeRepository,
    private val prayerAlarmManager: PrayerAlarmManager,
    private val dataStorePreference: DataStorePreference,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val dataStore: UserDataStore,
    private val connectivityObserver: ConnectivityObserver,
    private val downloader: Downloader,
) : ViewModel() {


    var isPrayerTimeRefreshing = MutableStateFlow(false)
        private set

    val prayerTimes: StateFlow<List<PrayerTimeModel>> = repository.prayerTimes

    private val _notificationState = MutableStateFlow(
        NotificationState(
            isFajrNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_FAJR_NOTIFICATION),
            isDhuhrNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_DHUHR_NOTIFICATION),
            isAsrNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_ASR_NOTIFICATION),
            isMaghribNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_MAGHRIB_NOTIFICATION),
            isIshaNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_ISHA_NOTIFICATION)
        )
    )
    val notificationState = combine(
        _notificationState,
        dataStore.selectedFajrNotification.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        ),
        dataStore.selectedDhuhrNotification.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        ),
        dataStore.selectedAsrNotification.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        ),
        dataStore.selectedMaghribNotification.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )
    ) { state, selectedFajrNotification, selectedDhuhrNotification, selectedAsrNotification, selectedMaghribNotification ->
        state.copy(
            selectedFajrAzan = selectedFajrNotification,
            selectedDhuhrAzan = selectedDhuhrNotification,
            selectedAsrAzan = selectedAsrNotification,
            selectedMaghribAzan = selectedMaghribNotification
        )
    }.combine(
        dataStore.selectedIshaNotification.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )
    ) { state, selectedIshaNotification ->
        state.copy(
            selectedIshaAzan = selectedIshaNotification
        )
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

            PrayerEvent.RefreshPrayer -> refreshPrayer()
        }
    }

    private fun refreshPrayer(){
        Log.d("PrayerTimeViewModel", "Refreshing prayer times")
        viewModelScope.launch {
            isPrayerTimeRefreshing.value = true
            val networkStatus = connectivityObserver.observer().first()
            if (networkStatus == ConnectivityObserver.Status.Available) {
                Log.d(
                    "PrayerTimeViewModel",
                    "Network is available, fetching new prayer times"
                )
                val apiTime = measureTimeMillis { repository.newPrayerTimesRequest() }
                Log.d("PrayerTimeViewModel", "API call took $apiTime ms")
                val minExecutionTime = 2000L
                if (apiTime < minExecutionTime) {
                    delay(minExecutionTime - apiTime)
                }
            } else {
                withContext(Dispatchers.IO) {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            isPrayerTimeRefreshing.value = false  // ✅ This will always execute
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
                        dataStorePreference.setPrayerNotification(
                            isNotification = _notificationState.value.isFajrNotification,
                            prayerKey = DataStorePreference.KEY_FAJR_NOTIFICATION
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
                        prayerAlarmManager.cancelAlarm(1)
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
                        dataStorePreference.setPrayerNotification(
                            isNotification = _notificationState.value.isDhuhrNotification,
                            prayerKey = DataStorePreference.KEY_DHUHR_NOTIFICATION
                        )
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
                        prayerAlarmManager.cancelAlarm(2)
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
                        dataStorePreference.setPrayerNotification(
                            isNotification = _notificationState.value.isAsrNotification,
                            prayerKey = DataStorePreference.KEY_ASR_NOTIFICATION
                        )
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
                        prayerAlarmManager.cancelAlarm(3)
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
                        dataStorePreference.setPrayerNotification(
                            isNotification = _notificationState.value.isMaghribNotification,
                            prayerKey = DataStorePreference.KEY_MAGHRIB_NOTIFICATION
                        )
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
                        prayerAlarmManager.cancelAlarm(4)
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
                        dataStorePreference.setPrayerNotification(
                            isNotification = _notificationState.value.isIshaNotification,
                            prayerKey = DataStorePreference.KEY_ISHA_NOTIFICATION
                        )
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
                        prayerAlarmManager.cancelAlarm(5)
                    }
                } else {
                    openAppSettings()
                }
            }

            NotificationEvent.RefreshNotificationState -> {
                viewModelScope.launch {
                    _notificationState.update {
                        it.copy(
                            isFajrNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_FAJR_NOTIFICATION),
                            isDhuhrNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_DHUHR_NOTIFICATION),
                            isAsrNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_ASR_NOTIFICATION),
                            isMaghribNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_MAGHRIB_NOTIFICATION),
                            isIshaNotification = dataStorePreference.getPrayerNotification(DataStorePreference.KEY_ISHA_NOTIFICATION)
                        )
                    }
                }
            }

            is NotificationEvent.OnFajrAzanClick -> {
                val isAzanPresent = isAzanExist(notificationEvent.fileName)
                viewModelScope.launch {
                    if (!isAzanPresent) {
                        downloadAzan(
                            azanUrl = notificationEvent.azanUrl,
                            fileName = notificationEvent.fileName
                        )
                    } else {
                        saveSelectedAzan(
                            sourceFileName = notificationEvent.fileName,
                            fileName = "fajrAzan.mp3",
                            prayerName = PrayerName.FAJR
                        )
                    }
                }
            }

            is NotificationEvent.OnDhuhrAzanClick -> {
                val isAzanPresent = isAzanExist(notificationEvent.fileName)
                viewModelScope.launch {
                    if (!isAzanPresent) {
                        downloadAzan(
                            azanUrl = notificationEvent.azanUrl,
                            fileName = notificationEvent.fileName
                        )
                    } else {
                        saveSelectedAzan(
                            sourceFileName = notificationEvent.fileName,
                            fileName = "dhurAzan.mp3",
                            prayerName = PrayerName.DHUHR
                        )
                    }
                }
            }

            is NotificationEvent.OnAsrAzanClick -> {
                val isAzanPresent = isAzanExist(notificationEvent.fileName)
                viewModelScope.launch {
                    if (!isAzanPresent) {
                        downloadAzan(
                            azanUrl = notificationEvent.azanUrl,
                            fileName = notificationEvent.fileName
                        )
                    } else {
                        saveSelectedAzan(
                            sourceFileName = notificationEvent.fileName,
                            fileName = "asrAzan.mp3",
                            prayerName = PrayerName.ASR
                        )
                    }
                }
            }

            is NotificationEvent.OnMaghribAzanClick -> {
                val isAzanPresent = isAzanExist(notificationEvent.fileName)
                viewModelScope.launch {
                    if (!isAzanPresent) {
                        downloadAzan(
                            azanUrl = notificationEvent.azanUrl,
                            fileName = notificationEvent.fileName
                        )
                    } else {
                        saveSelectedAzan(
                            sourceFileName = notificationEvent.fileName,
                            fileName = "maghribAzan.mp3",
                            prayerName = PrayerName.MAGHRIB
                        )
                    }
                }
            }

            is NotificationEvent.OnIshaAzanClick -> {
                val isAzanPresent = isAzanExist(notificationEvent.fileName)
                viewModelScope.launch {
                    if (!isAzanPresent) {
                        downloadAzan(
                            azanUrl = notificationEvent.azanUrl,
                            fileName = notificationEvent.fileName
                        )
                    } else {
                        saveSelectedAzan(
                            sourceFileName = notificationEvent.fileName,
                            fileName = "ishaAzan.mp3",
                            prayerName = PrayerName.ISHA
                        )
                    }
                }
            }

            is NotificationEvent.OnAzanPlayClick -> {
                val filePath =
                    "${context.filesDir}/$PARENT_FOLDER_NAME_DOWNLOAD/$DOWNLOADED_AZAN_FOLDER/${notificationEvent.fileName}.mp3"
                val file = File(filePath)
                if (file.exists()) {
                    _notificationState.update { state ->
                        state.copy(isAzanPlaying = state.isAzanPlaying.mapIndexed { index, isPlaying ->
                            index == notificationEvent.aazanIndex
                        })
                    }
                    Log.d(
                        "MediaPlayerHelper",
                        "Trying to play: $filePath, Exists: ${file.exists()}"
                    )
                    mediaPlayerHelper.playAzan(filePath)
                    mediaPlayerHelper.startAzan()
                } else {
                    downloadAzan(
                        azanUrl = notificationEvent.azanUrl,
                        fileName = notificationEvent.fileName
                    )
                }

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
                val isAzanExist = isSelectedAzanExist("fajrAzan.mp3")
                if (!isAzanExist && notificationEvent.index > 1) {
                    return
                }
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
                val isAzanExist = isSelectedAzanExist("dhurAzan.mp3")
                if (!isAzanExist && notificationEvent.index > 1) {
                    return
                }
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
                val isAzanExist = isSelectedAzanExist("asrAzan.mp3")
                if (!isAzanExist && notificationEvent.index > 1) {
                    return
                }
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
                val isAzanExist = isSelectedAzanExist("maghribAzan.mp3")
                if (!isAzanExist && notificationEvent.index > 1) {
                    return
                }
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
                val isAzanExist = isSelectedAzanExist("ishaAzan.mp3")
                if (!isAzanExist && notificationEvent.index > 1) {
                    return
                }
                _notificationState.update {
                    it.copy(
                        selectedIshaAzan = notificationEvent.index
                    )
                }
                viewModelScope.launch {
                    dataStore.setSelectedIshaNotification(notificationEvent.index)
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
            repository.getPrayerTimeByDate().first()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = "package:${context.packageName}".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }


    fun isAzanExist(fileName: String): Boolean {
        return isFilePresent(
            context = context,
            parentFolder = PARENT_FOLDER_NAME_DOWNLOAD,
            subFolderName = DOWNLOADED_AZAN_FOLDER,
            fileName = "${fileName}.mp3"
        )
    }

    fun isSelectedAzanExist(azan: String): Boolean {
        return isFilePresent(
            context = context,
            parentFolder = PARENT_FOLDER_NAME_DOWNLOAD,
            subFolderName = SELECTED_ATHANS_SUB_FOLDER_NAME,
            fileName = azan
        )
    }


    private fun downloadAzan(azanUrl: String, fileName: String) {
        viewModelScope.launch {
            val networkStatus = connectivityObserver.observer().first()
            if (networkStatus == ConnectivityObserver.Status.Available) {
                _notificationState.update { it.copy(isAzanDownloading = true) }
                val result = downloader.downloadFile(
                    url = azanUrl,
                    mimeType = "audio/mpeg",
                    title = fileName
                )
                if (result >= 1L) {
                    _notificationState.update { it.copy(isAzanDownloading = false) }
                    Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    _notificationState.update { it.copy(isAzanDownloading = false) }
                }
            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveSelectedAzan(
        sourceFileName: String,
        fileName: String,
        prayerName: PrayerName
    ) {
        val success = saveMp3File(
            context = context,
            sourceFilePath = "${context.filesDir}/$PARENT_FOLDER_NAME_DOWNLOAD/$DOWNLOADED_AZAN_FOLDER/${sourceFileName}.mp3",
            parentFolderName = PARENT_FOLDER_NAME_DOWNLOAD,
            subFolderName = SELECTED_ATHANS_SUB_FOLDER_NAME,
            fileName = fileName
        )
        if (success) {
            dataStore.savePrayerNotificationType(
                prayerName = prayerName,
                NotificationType.AZAN
            )
            Log.d(
                "PrayerTimeViewModel",
                "Aazn $fileName: , NotificationType: ${NotificationType.AZAN}"
            )
        } else {
            Log.e("PrayerTimeViewModel", "Failed to save MP3.")
        }
    }
}
