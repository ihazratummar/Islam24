package com.hazrat.prayertime.data.repository

import android.content.Context
import android.content.Intent
import com.hazrat.database.dao.PrayerTimeDao
import com.hazrat.database.entity.LocationEntity
import com.hazrat.database.entity.PrayerTimeEntity
import com.hazrat.domain.repository.PrayerSettingRepository
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import com.hazrat.model.PrayerTimeModel
import com.hazrat.prayertime.data.mapper.toEntityList
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-12-2024
 */

class PrayerTimeRepositoryImpl(
    private val api: PrayerTimeApi,
    private val locationRepository: LocationRepository,
    private val prayerSettingRepository: PrayerSettingRepository,
    private val prayerTimeDao: PrayerTimeDao,
    private val context: Context,
    private val connectivityObserver: ConnectivityObserver
) : PrayerTimeRepository {

    private val _prayerTimes = MutableStateFlow<List<PrayerTimeModel>>(emptyList())
    override val prayerTimes = _prayerTimes.asStateFlow()

    private val _prayerTimeByDate = MutableStateFlow<List<PrayerTimeModel>>(emptyList())
    override val prayerTimeByDate = _prayerTimeByDate.asStateFlow()

    override suspend fun newPrayerTimesRequest(): List<PrayerTimeEntity> {
        return try {
            var latitude: Double
            var longitude : Double
            when(val result  =  locationRepository.getCurrentLocation()){
                is LocationResult.Error -> {
                    latitude = 21.42
                    longitude = 39.82
                }
                is LocationResult.Success -> {
                    latitude = result.location.latitude
                    longitude = result.location.longitude
                }
            }
            Timber.tag("NewPrayerTimeRepositoryImpl")
                .d("Latitude: $latitude, Longitude: $longitude")
            val methodList = prayerSettingRepository.getCalculationMethod().firstOrNull()
            Timber.tag("NewPrayerTimeRepositoryImpl").d("Method: $methodList")
            val juristicList = prayerSettingRepository.getJuristicMethod().firstOrNull()
            val methodValue = methodList?.method ?: 1
            val schoolValue = juristicList?.school ?: 0

            val year = DateUtil.getCurrentYear()
            val month = DateUtil.getCurrentMonth()

            val networkStatus = connectivityObserver.observer().first()

            if (networkStatus != ConnectivityObserver.Status.Available) {
                throw IOException("No network available to fetch prayer times.")
            }
            val apiResponse = api.newPrayerTimesRequest(
                year = year,
                month = month,
                latitude = "$latitude",
                longitude = "$longitude",
                method = methodValue,
                school = schoolValue,
                annual = true
            )
            Timber.tag("NewPrayerTimeRepositoryImpl").d(
                "Api called ${
                    apiResponse.data.values.flatten()
                        .find { it.date.gregorian.date == DateUtil.getCurrentDate() }
                }"
            )
            prayerTimeDao.insertAllPrayerTimes(apiResponse.data.values.flatten().toEntityList())
            apiResponse.data.values.flatten().toEntityList()
        } catch (e: HttpException) {
            Timber.tag("NewPrayerTimeRepositoryImpl").e("HTTP Error: ${e.code()} - ${e.message()}")
            throw e
        } catch (e: Exception) {
            Timber.tag("NewPrayerTimeRepositoryImpl").e("Unexpected Error: ${e.message}")
            throw e
        }
    }


    override fun getAllPrayer(): Flow<List<PrayerTimeModel>> =
        prayerTimeDao.getAllPrayer().flowOn(Dispatchers.IO)
            .conflate().map { it.toPrayerModelList() }

    override fun getPrayerTimeByDate(): Flow<List<PrayerTimeModel>> {
        val today =
            DateUtil.getCurrentDate() // Utility function to get the current date as a String
        return prayerTimeDao.getPrayerTimesFromDate(today) // Fetch the list of prayer times from DAO
            .distinctUntilChanged() // Avoid unnecessary recomputations when data hasn't changed
            .map { prayerTimes ->
                // Optionally update shared state with the list, if needed
                _prayerTimeByDate.value = prayerTimes.toPrayerModelList()
                Timber.tag("NewPrayerTimeRepositoryImpl").d("Prayer times for today: $prayerTimes")
                prayerTimes.toPrayerModelList() // Emit the list of prayer times
            }
    }


    override fun sharePrayerTimes(prayerTimes: List<PrayerTimeModel>) {
        val today = DateUtil.getCurrentDay() - 1
        val prayerTimeIndex = prayerTimes[today]
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
                val currentYear = DateUtil.getCurrentYear()
                if (prayerList.isEmpty() || currentYear != prayerList[0].gregorianYear.toInt()) {
                    val networkStatus = connectivityObserver.observer().first()
                    if (networkStatus == ConnectivityObserver.Status.Available) {
                        newPrayerTimesRequest()
                    }
                } else {
                    _prayerTimes.value = prayerList
                }
            }
    }

    override fun getHijriDay(): Int {
        val currentGregorianDay = DateUtil.getCurrentDate()

        val hijriDateEntity = _prayerTimes.value.indexOfFirst {
            it.gregorianDate == currentGregorianDay
        }
        val hijriDate = _prayerTimes.value[hijriDateEntity].hijriDay
        return hijriDate
    }
}