//PrayerTimeViewmodl.kt

package com.hazrat.prayer.ui.prayertime


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
import com.hazrat.domain.repository.PrayerTimeRepositoryNew
import com.hazrat.downloader.Downloader
import com.hazrat.downloader.MyFileUtils.saveMp3File
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.Prayer
import com.hazrat.notification.MediaPlayerHelper
import com.hazrat.notification.PrayerAlarmManager
import com.hazrat.prayer.ui.notification.NotificationEvent
import com.hazrat.prayer.ui.notification.NotificationState
import com.hazrat.usecase.GetDailyPrayerStatusUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetTodayPrayerTimeUseCase
import com.hazrat.usecase.TogglePrayerUseCase
import com.hazrat.utils.Constants.DOWNLOADED_AZAN_FOLDER
import com.hazrat.utils.Constants.PARENT_FOLDER_NAME_DOWNLOAD
import com.hazrat.utils.Constants.SELECTED_ATHANS_SUB_FOLDER_NAME
import com.hazrat.utils.MyFileUtils.isFilePresent
import com.hazrat.utils.network.ConnectivityObserver
import com.hazrat.utils.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.Clock
import java.time.LocalDate


class PrayerTimeViewModel(
    private val context: Context,
    private val repository: PrayerTimeRepositoryNew,
    private val prayerAlarmManager: PrayerAlarmManager,
    private val dataStorePreference: DataStorePreference,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val dataStore: UserDataStore,
    private val connectivityObserver: ConnectivityObserver,
    private val downloader: Downloader,
    private val getTodayPrayerTimeUseCase: GetTodayPrayerTimeUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getDailyPrayerStatus: GetDailyPrayerStatusUseCase,
    private val togglePrayerUseCase: TogglePrayerUseCase,
    private val clock: Clock = Clock.systemDefaultZone()
) : ViewModel() {

    private val today get() = LocalDate.now(clock)

    val dailyStatus: StateFlow<DailyPrayerStatus?> =
        getDailyPrayerStatus.invoke(date = today)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _pendingToggles = MutableStateFlow<Set<Prayer>>(emptySet())


    private val _uiState = MutableStateFlow(PrayerTimeUiState())
    val uiState: StateFlow<PrayerTimeUiState> = _uiState.asStateFlow()


    private val _events = Channel<PrayerTimeUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

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


    init {
        viewModelScope.launch(Dispatchers.IO) {
            observeAndSync()
        }
        loadLocationName()
    }

    private fun loadLocationName() {
        viewModelScope.launch {
            getLocationNameUseCase.invoke().collectLatest { locationName ->
                _uiState.update {
                    it.copy(
                        locationNane = locationName
                    )
                }
            }
        }
    }

    private fun observeAndSync() {
        viewModelScope.launch {
            getTodayPrayerTimeUseCase.invoke().collectLatest {prayerData ->
                _uiState.update {
                    it.copy(
                        prayerTimes = prayerData
                    )
                }
            }
        }
    }

    fun onEvent(prayerEvent: PrayerEvent) {
        when (prayerEvent) {
            PrayerEvent.SharePrayer -> {
//                repository.sharePrayerTimes(prayerTimes.value)
            }

            PrayerEvent.RefreshPrayer -> refreshPrayer()
            is PrayerEvent.LogPrayer -> {
                if (prayerEvent.prayer in _pendingToggles.value) return
                viewModelScope.launch {
                    _pendingToggles.update { it + prayerEvent.prayer }
                    try {
                        val currentlyLogged = dailyStatus.value?.isLogged(prayer = prayerEvent.prayer)?:false
                        togglePrayerUseCase(prayer = prayerEvent.prayer, currentlyLogged = currentlyLogged, date = prayerEvent.date)
                    }finally {
                        _pendingToggles.update { it - prayerEvent.prayer }
                    }
                }
            }
        }
    }

    private fun refreshPrayer() {
        Log.d("PrayerTimeViewModel", "Refreshing prayer times")
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            when (val result = repository.refreshPrayerTimes()) {
                is Result.Error -> {
                    _events.send(PrayerTimeUiEvent.ShowError(result.error.toString()))
                    _uiState.update { it.copy(isRefreshing = false) }
                }

                is Result.Success -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
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
                        dataStorePreference.setPrayerNotification(
                            isNotification = _notificationState.value.isFajrNotification,
                            prayerKey = DataStorePreference.KEY_FAJR_NOTIFICATION
                        )
                    }
                    if (_notificationState.value.isFajrNotification) {

                        val fajr = _uiState.value.prayerTimes?.fajrTime!!
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
                        val dhuhrTime = _uiState.value.prayerTimes?.dhuhrTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            dhuhrTime?.let {
                                prayerAlarmManager.setDhuhrPrayerAlarm(dhuhrTime)
                            }
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
                        val asrTime = _uiState.value.prayerTimes?.asrTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            asrTime?.let {
                                prayerAlarmManager.setAsrPrayerAlarm(asrTime)
                            }
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
                        val maghribTime =  _uiState.value.prayerTimes?.maghribTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            maghribTime?.let {
                                prayerAlarmManager.setMaghribPrayerAlarm(maghribTime)
                            }
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
                        val ishaTime = _uiState.value.prayerTimes?.ishaTime
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            ishaTime?.let {
                                prayerAlarmManager.setIshaPrayerAlarm(ishaTime)
                            }
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
                            isFajrNotification = dataStorePreference.getPrayerNotification(
                                DataStorePreference.KEY_FAJR_NOTIFICATION
                            ),
                            isDhuhrNotification = dataStorePreference.getPrayerNotification(
                                DataStorePreference.KEY_DHUHR_NOTIFICATION
                            ),
                            isAsrNotification = dataStorePreference.getPrayerNotification(
                                DataStorePreference.KEY_ASR_NOTIFICATION
                            ),
                            isMaghribNotification = dataStorePreference.getPrayerNotification(
                                DataStorePreference.KEY_MAGHRIB_NOTIFICATION
                            ),
                            isIshaNotification = dataStorePreference.getPrayerNotification(
                                DataStorePreference.KEY_ISHA_NOTIFICATION
                            )
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
