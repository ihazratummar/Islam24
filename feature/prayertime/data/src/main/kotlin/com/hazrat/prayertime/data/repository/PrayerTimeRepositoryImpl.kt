package com.hazrat.prayertime.data.repository

import android.content.Context
import android.content.Intent
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.hazrat.database.dao.PrayerTimeDao
import com.hazrat.database.entity.PrayerTimeEntity
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerSettingRepository
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import com.hazrat.model.MinimalPrayerData
import com.hazrat.model.PrayerTimeModel
import com.hazrat.prayertime.data.mapper.toEntityList
import com.hazrat.prayertime.data.mapper.toMinimalPrayerData
import com.hazrat.prayertime.data.mapper.toPrayerModelList
import com.hazrat.remote.api.PrayerTimeApi
import com.hazrat.ui.R
import com.hazrat.utils.DateUtil
import com.hazrat.utils.network.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-12-2024
 *
 * Implementation of [PrayerTimeRepository].
 *
 * Responsibilities:
 *  - Fetch yearly prayer times from the remote API and persist them in the local database.
 *  - Serve prayer times to the rest of the app exclusively via observable [Flow]s.
 *  - Surface all failures as [PrayerTimeResult] instead of throwing raw exceptions.
 *
 * Threading: all DB / network work runs on [DispatcherProvider.io].
 * The repository itself holds NO mutable UI state — that belongs in the ViewModel.
 */


class PrayerTimeRepositoryImpl(
    private val api: PrayerTimeApi,
    private val locationRepository: LocationRepository,
    private val prayerSettingRepository: PrayerSettingRepository,
    private val prayerTimeDao: PrayerTimeDao,
    private val context: Context,
    private val connectivityObserver: ConnectivityObserver,
    private val userDataStore: UserDataStore
) : PrayerTimeRepository {

    private val _prayerTimes = MutableStateFlow<List<PrayerTimeModel>>(emptyList())
    override val prayerTimes = _prayerTimes.asStateFlow()

    override suspend fun newPrayerTimesRequest(): List<PrayerTimeEntity> {
        val currentYear = UmmalquraCalendar().get(UmmalquraCalendar.YEAR)
        val currentMonth = DateUtil.getCurrentMonth()

        // Fetch current year
        val entities = fetchAndSaveYearlyPrayerTimes(currentYear)

        // If we are in December, also pre-fetch next year
        if (currentMonth == 12) {
            try {
                fetchAndSaveYearlyPrayerTimes(currentYear + 1)
            } catch (e: Exception) {
                Timber.tag("NewPrayerTimeRepositoryImpl")
                    .e("Failed to pre-fetch next year: ${e.message}")
            }
        }

        return entities
    }

    override suspend fun getTodayPrayerTime(): Flow<MinimalPrayerData> {
        val today = DateUtil.getCurrentDate()
        return prayerTimeDao.getPrayerTimeForToday(currentDate = today)
            .mapLatest {prayerTimesMinimal ->
                if (prayerTimesMinimal == null){
                    val prayerList = newPrayerTimesRequest()
                    val todayPrayer = prayerList.find { it.gregorianDate == today }
                    todayPrayer?.toMinimalPrayerData()?: MinimalPrayerData()
                }else{
                    prayerTimesMinimal.toMinimalPrayerData()
                }

            }

    }

    private suspend fun fetchAndSaveYearlyPrayerTimes(
        year: Int
    ): List<PrayerTimeEntity> {
        return try {
            var latitude: Double
            var longitude: Double
            when (val result = locationRepository.getCurrentLocation()) {
                is LocationResult.Error -> {
                    latitude = 21.42
                    longitude = 39.82
                }

                is LocationResult.Success -> {
                    latitude = result.location.latitude
                    longitude = result.location.longitude
                }
            }

            val calculationMethod = userDataStore.getPrayerCalculationMethod()
            val juristicMethod = userDataStore.getPrayerJuristicMethod()

            val networkStatus = connectivityObserver.observer().first()
            if (networkStatus != ConnectivityObserver.Status.Available) {
                throw IOException("No network available to fetch prayer times for $year.")
            }

            val apiResponse = api.newPrayerTimesRequest(
                year = year,
                latitude = "$latitude",
                longitude = "$longitude",
                method = calculationMethod,
                school = juristicMethod
            )

            val entities = apiResponse.data.values.flatten().toEntityList()
            prayerTimeDao.insertAllPrayerTimes(entities)
            entities
        } catch (e: HttpException) {
            Timber.tag("NewPrayerTimeRepositoryImpl").e("HTTP Error for $year: ${e.code()}")
            throw e
        } catch (e: Exception) {
            Timber.tag("NewPrayerTimeRepositoryImpl").e("Error fetching $year: ${e.message}")
            throw e
        }
    }


    override fun getAllPrayer(): Flow<List<PrayerTimeModel>> =
        prayerTimeDao.getAllPrayer().flowOn(Dispatchers.IO)
            .conflate().map { it.toPrayerModelList() }

    override fun sharePrayerTimes(prayerTimes: List<PrayerTimeModel>) {
        // Find today's prayer time in the list by matching Gregorian date
        val today = DateUtil.getCurrentDate()
        val prayerTimeIndex = prayerTimes.find { it.gregorianDate == today } ?: return

        val text =
            "Today's prayer times\n ${prayerTimeIndex.gregorianDate}// ${prayerTimeIndex.hijriDate}\n\n" +
                    "${context.getString(R.string.fajr)}: ${
                        DateUtil.dateLongToString(
                            prayerTimeIndex.fajrTime
                        )
                    }\n" +
                    "${context.getString(R.string.dhuhr)}: ${
                        DateUtil.dateLongToString(
                            prayerTimeIndex.dhuhrTime
                        )
                    }\n" +
                    "${context.getString(R.string.asr)}: ${DateUtil.dateLongToString(prayerTimeIndex.asrTime)}\n" +
                    "${context.getString(R.string.maghrib)}: ${
                        DateUtil.dateLongToString(
                            prayerTimeIndex.maghribTime
                        )
                    }\n" +
                    "${context.getString(R.string.isha_a)}: ${
                        DateUtil.dateLongToString(
                            prayerTimeIndex.ishaTime
                        )
                    }\n\n" +
                    "For More Visit, https://play.google.com/store/apps/details?id=com.hazrat.islam24"
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val shareIntent = Intent.createChooser(intent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(shareIntent)
    }

    override suspend fun getAllPrayerTimes() {
        getAllPrayer().distinctUntilChanged()
            .collectLatest { prayerList: List<PrayerTimeModel> ->
                val currentYear = UmmalquraCalendar().get(UmmalquraCalendar.YEAR)

                // Migration Check: If data exists but in old format (dd-MM-yyyy), re-fetch
                val hasOldFormat = prayerList.any { it.gregorianDate.indexOf("-") == 2 }
                val hasCurrentYearData = prayerList.any { it.gregorianYear.toInt() == currentYear }

                if (prayerList.isEmpty() || hasOldFormat || !hasCurrentYearData) {
                    val networkStatus = connectivityObserver.observer().first()
                    if (networkStatus == ConnectivityObserver.Status.Available) {
                        if (hasOldFormat) {
                            prayerTimeDao.deleteAllPrayer()
                        }
                        newPrayerTimesRequest()
                    }
                } else {
                    _prayerTimes.value = prayerList

                    // If we are in late December and don't have next year's data, trigger fetch
                    val currentMonth = UmmalquraCalendar().get(UmmalquraCalendar.MONTH) + 1
                    if (currentMonth == 12) {
                        val hasNextYearData =
                            prayerList.any { it.hijriYear == currentYear + 1 }
                        if (!hasNextYearData) {
                            val networkStatus = connectivityObserver.observer().first()
                            if (networkStatus == ConnectivityObserver.Status.Available) {
                                try {
                                    fetchAndSaveYearlyPrayerTimes(currentYear + 1 )
                                } catch (e: Exception) {
                                    Timber.tag("NewPrayerTimeRepositoryImpl")
                                        .e("Auto pre-fetch failed: ${e.message}")
                                }
                            }
                        }
                    }
                }
            }
    }

    override fun getHijriDay(): Int {
        val currentGregorianDay = DateUtil.getCurrentDate()

        val hijriDateEntity = _prayerTimes.value.indexOfFirst {
            it.gregorianDate == currentGregorianDay
        }
        if (hijriDateEntity == -1) return 1
        val hijriDate = _prayerTimes.value[hijriDateEntity].hijriDay
        return hijriDate
    }
}
