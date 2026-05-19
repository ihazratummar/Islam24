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
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerTimeRepositoryNew
import com.hazrat.downloader.Downloader
import com.hazrat.downloader.MyFileUtils.saveMp3File
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.Prayer
import com.hazrat.notification.MediaPlayerHelper
import com.hazrat.notification.PrayerAlarmScheduler
import com.hazrat.prayer.ui.notification.NotificationState
import com.hazrat.usecase.GetDailyPrayerStatusUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetPrayerNotificationStateUseCase
import com.hazrat.usecase.GetTodayPrayerTimeUseCase
import com.hazrat.usecase.PrayerNotificationEnabledUseCase
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
import java.time.Clock
import java.time.LocalDate


class PrayerTimeViewModel(
    private val context: Context,
    private val repository: PrayerTimeRepositoryNew,
    private val prayerAlarmManager: PrayerAlarmScheduler,
    private val dataStorePreference: DataStorePreference,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val dataStore: UserDataStore,
    private val connectivityObserver: ConnectivityObserver,
    private val downloader: Downloader,
    private val getTodayPrayerTimeUseCase: GetTodayPrayerTimeUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getDailyPrayerStatus: GetDailyPrayerStatusUseCase,
    private val togglePrayerUseCase: TogglePrayerUseCase,
    private val clock: Clock = Clock.systemDefaultZone(),
    private val prayerNotificationEnabledUseCase: PrayerNotificationEnabledUseCase,
    private val getPrayerNotificationStateUseCase: GetPrayerNotificationStateUseCase
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            observeAndSync()
        }
        loadLocationName()
    }

    private val _notificationState = MutableStateFlow(NotificationState())

    val notificationState = combine(
        _notificationState,
        getPrayerNotificationStateUseCase.invoke()
    ) { state, notificationState ->
        state.copy(
            isFajrNotification = notificationState.fajr,
            isDhuhrNotification = notificationState.dhuhr,
            isAsrNotification = notificationState.asr,
            isMaghribNotification = notificationState.maghrib,
            isIshaNotification = notificationState.isha
        )
    }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), NotificationState())


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
            getTodayPrayerTimeUseCase.invoke().collectLatest { prayerData ->
                _uiState.update {
                    it.copy(
                        prayerTimes = prayerData
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun onEvent(prayerEvent: PrayerEvent) {
        when (prayerEvent) {
            PrayerEvent.SharePrayer -> {

            }

            PrayerEvent.RefreshPrayer -> refreshPrayer()
            is PrayerEvent.LogPrayer -> {
                if (prayerEvent.prayer in _pendingToggles.value) return
                viewModelScope.launch {
                    _pendingToggles.update { it + prayerEvent.prayer }
                    try {
                        val currentlyLogged =
                            dailyStatus.value?.isLogged(prayer = prayerEvent.prayer) ?: false
                        togglePrayerUseCase(
                            prayer = prayerEvent.prayer,
                            currentlyLogged = currentlyLogged,
                            date = prayerEvent.date
                        )
                    } finally {
                        _pendingToggles.update { it - prayerEvent.prayer }
                    }
                }
            }

            is PrayerEvent.PrayerNotificationToggle -> {
                viewModelScope.launch {
                    if (checkExactAlarmPermission()){
                        prayerNotificationEnabledUseCase.invoke(
                            prayer = prayerEvent.prayer,
                            enabled = prayerEvent.enabled
                        )
                        if (prayerEvent.enabled) {
                            prayerAlarmManager.setPrayerAlarm(
                                prayerName = prayerEvent.prayer,
                                prayerEvent.prayerTIme
                            )
                        } else {
                            prayerAlarmManager.cancelAlarm(prayerEvent.prayer.notificationCode)
                        }
                    }else{
                        openAppSettings()
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
        prayerName: Prayer
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
