package com.hazrat.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.database.dao.DuaDao
import com.hazrat.database.dao.PrayerLogDao
import com.hazrat.database.dao.QuranDao
import com.hazrat.datastore.DataStorePreference
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.usecase.GetIslamicEventsUseCase
import com.hazrat.usecase.GetLocationNameUseCase
import com.hazrat.usecase.GetNextFridayTime
import com.hazrat.usecase.GetUpcomingMainIslamicEventUseCase
import com.hazrat.usecase.prayer.GetDailyPrayerStatusUseCase
import com.hazrat.usecase.prayer.GetTodayPrayerTimeUseCase
import com.hazrat.utils.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

/**
 * @author Hazrat Ummar Shaikh
 */

class HomeViewModel(
    private val getTodayPrayerTimeUseCase: GetTodayPrayerTimeUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getUpcomingMainIslamicEventUseCase: GetUpcomingMainIslamicEventUseCase,
    private val islamicEventsUseCase: GetIslamicEventsUseCase,
    private val getNextFridayTime: GetNextFridayTime,
    private val dailyPrayerStatusUseCase: GetDailyPrayerStatusUseCase,
    private val dataStorePreference: DataStorePreference? = null,
    private val quranDao: QuranDao? = null,
    private val duaDao: DuaDao? = null,
    private val prayerLogDao: PrayerLogDao? = null,
    private val clock: Clock = Clock.systemDefaultZone(),
) : ViewModel() {

    val today: LocalDate = LocalDate.now(clock)
    private val todayDateStr: String = today.format(DateTimeFormatter.ISO_LOCAL_DATE)

    private val _locationName = MutableStateFlow(LocationName())
    val locationName = _locationName.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    val dailyStatus: StateFlow<DailyPrayerStatus?> = dailyPrayerStatusUseCase.invoke(today).stateIn(
        viewModelScope, started = SharingStarted.WhileSubscribed(5000), null
    )

    init {
        refreshLocation()
        getTodayPrayer()
        loadMainIslamicEvent()
        loadIslamicEvents()
        loadDailyVerse()
        loadDailyDua()
        loadWeeklyPrayerConsistency()

        viewModelScope.launch {
            getNextFridayTime.invoke().collectLatest { time ->
                _homeState.update {
                    it.copy(
                        fridayTime = time
                    )
                }
            }
        }
    }

    private fun loadDailyVerse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (dataStorePreference != null && quranDao != null) {
                    val cachedVerse = dataStorePreference.getDailyVerse(todayDateStr)
                    val surahNumber: Int
                    val ayahNumber: Int

                    if (cachedVerse != null) {
                        surahNumber = cachedVerse.first
                        ayahNumber = cachedVerse.second
                    } else {
                        surahNumber = 29
                        ayahNumber = 45
                        dataStorePreference.saveDailyVerse(todayDateStr, surahNumber, ayahNumber)
                    }

                    val surah = quranDao.getSurahByNumber(surahNumber).firstOrNull()
                    val ayahs = quranDao.getAllAyah(surahNumber).firstOrNull()
                    val targetAyah = ayahs?.find { it.ayahNumber == ayahNumber } ?: ayahs?.firstOrNull()

                    if (targetAyah != null) {
                        _homeState.update {
                            it.copy(
                                dailyVerse = DailyVerseData(
                                    surahName = surah?.nameTransliterated ?: "Al-Ankabut",
                                    verseNumber = targetAyah.ayahNumber,
                                    arabicText = targetAyah.arabicText,
                                    englishTranslation = targetAyah.englishTranslation
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading daily verse from DB", e)
            }
        }
    }

    private fun loadDailyDua() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (dataStorePreference != null && duaDao != null) {
                    val cachedDua = dataStorePreference.getDailyDua(todayDateStr)
                    val categoryId: Int
                    val duaId: Int

                    if (cachedDua != null) {
                        categoryId = cachedDua.first
                        duaId = cachedDua.second
                    } else {
                        categoryId = 1
                        duaId = 1
                        dataStorePreference.saveDailyDua(todayDateStr, categoryId, duaId)
                    }

                    val categories = duaDao.getDuaCategory().firstOrNull()
                    val targetCategory = categories?.find { it.id == categoryId } ?: categories?.firstOrNull()
                    val duaItems = duaDao.getDua(categoryId).firstOrNull()
                    val targetDua = duaItems?.find { it.id == duaId } ?: duaItems?.firstOrNull()

                    if (targetDua != null) {
                        _homeState.update {
                            it.copy(
                                dailyDua = DailyDuaData(
                                    categoryTitle = targetCategory?.title ?: "Upon Waking Up",
                                    arabicText = targetDua.arabicText,
                                    transliteration = targetDua.transliteration,
                                    translation = targetDua.translation,
                                    reference = targetDua.reference.ifBlank { "Sahih al-Bukhari" }
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading daily dua from DB", e)
            }
        }
    }

    private fun loadWeeklyPrayerConsistency() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sunday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                val daysList = (0..6).map { sunday.plusDays(it.toLong()) }
                val currentDayIndex = (today.dayOfWeek.value % 7) // 0 = Sun, 6 = Sat

                val flows = daysList.map { day -> dailyPrayerStatusUseCase.invoke(day) }

                combine(flows) { statuses ->
                    val counts = statuses.map { it.loggedPrayers.size }
                    val total = counts.sum()
                    WeeklyPrayerStats(
                        dailyCounts = counts,
                        currentDayIndex = currentDayIndex,
                        totalCompleted = total,
                        totalTarget = 35
                    )
                }.collectLatest { stats ->
                    _homeState.update {
                        it.copy(weeklyPrayerStats = stats)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading weekly prayer consistency", e)
            }
        }
    }

    private fun loadMainIslamicEvent() {
        val event = getUpcomingMainIslamicEventUseCase.invoke()
        _homeState.update {
            it.copy(
                upcomingIslamicEvent = event
            )
        }
    }

    fun refreshLocation() {
        _homeState.update { it.copy(isLocationLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            getLocationNameUseCase.invoke().collectLatest { locationName ->
                _locationName.value = LocationName(address = locationName.locationName)
                _homeState.update { it.copy(isLocationLoading = false) }
            }
        }
    }

    private fun loadIslamicEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            islamicEventsUseCase.invoke().collectLatest { events ->
                _homeState.update {
                    it.copy(
                        islamicEventsInfoModel = events
                    )
                }
                Log.d("HomeViewModel", "List $events")
            }
        }
    }

    private fun getTodayPrayer() {
        viewModelScope.launch(Dispatchers.IO) {
            getTodayPrayerTimeUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> {
                        _homeState.update {
                            it.copy(
                                prayerData = result.data
                            )
                        }
                        Log.d("HomeViewModel", "Minimal Prayer Data ${result.data}")
                    }
                }
            }
        }
    }
}