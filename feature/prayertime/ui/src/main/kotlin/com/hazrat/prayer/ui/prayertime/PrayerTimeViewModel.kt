//PrayerTimeViewmodl.kt

package com.hazrat.prayer.ui.prayertime


import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.Prayer
import com.hazrat.notification.PrayerAlarmScheduler
import com.hazrat.prayer.ui.notification.NotificationState
import com.hazrat.usecase.GetDailyPrayerStatusUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetPrayerNotificationStateUseCase
import com.hazrat.usecase.GetPrayerTimeWindowForDaysUseCase
import com.hazrat.usecase.PrayerNotificationEnabledUseCase
import com.hazrat.usecase.TogglePrayerUseCase
import com.hazrat.utils.DateUtil
import com.hazrat.utils.HijriDateUtils
import com.hazrat.utils.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


class PrayerTimeViewModel(
    private val context: Context,
    private val repository: PrayerTimeRepository,
    private val prayerAlarmManager: PrayerAlarmScheduler,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getDailyPrayerStatusUseCase: GetDailyPrayerStatusUseCase,
    private val togglePrayerUseCase: TogglePrayerUseCase,
    private val clock: Clock = Clock.systemDefaultZone(),
    private val prayerNotificationEnabledUseCase: PrayerNotificationEnabledUseCase,
    private val getPrayerNotificationStateUseCase: GetPrayerNotificationStateUseCase,
    private val getPrayerTimeWindowForDaysUseCase: GetPrayerTimeWindowForDaysUseCase
) : ViewModel() {

    companion object {
        private const val PREFETCH_THRESHOLD = 2
        private const val WINDOW_SIZE = 15
    }

    private val today get() = LocalDate.now(clock)

    private val _uiState = MutableStateFlow(PrayerTimeUiState())
    val uiState: StateFlow<PrayerTimeUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val dailyStatus: StateFlow<DailyPrayerStatus?> =
        _uiState.map { state ->
            state.pages.getOrNull(
                if (state.selectedIndex == -1) 0 else state.selectedIndex
            )?.let { page ->
                Instant.ofEpochSecond(page.timeStamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            } ?: LocalDate.now(clock)
        }
            .distinctUntilChanged()
            .flatMapLatest { date ->
                Log.d("PrayerTimeViewModel", "PrayerStatus $date")
                getDailyPrayerStatusUseCase.invoke(date = date)
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _pendingToggles = MutableStateFlow<Set<Prayer>>(emptySet())


    private val _events = Channel<PrayerTimeUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

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


    private val anchorHijri = MutableStateFlow(HijriDateUtils.today())
    private var windowJob: Job? = null

    init {
        loadLocationName()
        observePrayerPageWindow()
    }

    private fun observePrayerPageWindow() {
        windowJob?.cancel()
        windowJob = viewModelScope.launch(Dispatchers.IO) {
            anchorHijri.collectLatest { anchorHijri ->
                getPrayerTimeWindowForDaysUseCase.invoke(
                    anchor = anchorHijri,
                    daysBefore = WINDOW_SIZE / 2,
                    daysAfter = WINDOW_SIZE / 2
                ).collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(isLoading = false, isFetchingNextYear = false)
                            }
                            _events.trySend(
                                PrayerTimeUiEvent.ShowError(result.error.toString())
                            )
                        }

                        is Result.Success -> {
                            val pages = result.data
                            val todayIdx = pages.indexOfFirst {
                                it.hijriDay == HijriDateUtils.today().day &&
                                        it.hijriMonthNumber == HijriDateUtils.today().month &&
                                        it.hijriYear == HijriDateUtils.today().year
                            }.coerceAtLeast(0)
                            _uiState.update {
                                it.copy(
                                    pages = pages,
                                    prayerTimes = pages.getOrNull(todayIdx),
                                    selectedIndex = if (it.selectedIndex == -1) todayIdx
                                    else it.selectedIndex,
                                    isLoading = false,
                                    isFetchingNextYear = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    private fun loadLocationName() {
        viewModelScope.launch {
            getLocationNameUseCase.invoke().collectLatest { locationName ->
                _uiState.update {
                    it.copy(
                        locationNane = locationName.locationName
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
                    if (checkExactAlarmPermission()) {
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
                    } else {
                        openAppSettings()
                    }
                }
            }

            is PrayerEvent.OnPageChanged -> {
                onPageChanged(index = prayerEvent.index)
            }
        }
    }


    private fun onPageChanged(index: Int) {
        val pages = _uiState.value.pages
        if (pages.isEmpty()) return

        _uiState.update { it.copy(selectedIndex = index) }

        val distanceFromEnd = pages.lastIndex - index
        val distanceFromStart = index

        if (distanceFromEnd <= PREFETCH_THRESHOLD || distanceFromStart <= PREFETCH_THRESHOLD) {
            val page = pages.getOrNull(index) ?: return
            if (page.hijriMonthNumber == 12 && distanceFromEnd <= PREFETCH_THRESHOLD) {
                _uiState.update { it.copy(isFetchingNextYear = true) }
            }
            anchorHijri.value = HijriDateUtils.fromMinimalPrayerData(data = page)
        }
    }


    private fun refreshPrayer() {
        Log.d("PrayerTimeViewModel", "Refreshing prayer times")
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                when (val result = repository.refreshPrayerTimes()) {
                    is Result.Error -> {
                        _events.send(PrayerTimeUiEvent.ShowError(result.error.toString()))
                        _uiState.update { it.copy(isRefreshing = false) }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(isRefreshing = false) }
                        Log.d("PrayerTimeViewModel", "Data - ${result.data}")
                    }
                }
            } catch (e: Exception) {
                Log.e("PrayerTimeViewModel", "Crash Logs - ${e.message}")
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


}
