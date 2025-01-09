package com.hazrat.islam24.core.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.dao.PrayerTimeDao
import com.hazrat.islam24.core.data.entity.LocationEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.data.mapper.prayertime_mappers.toEntityList
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.core.remote.api.PrayerTimeApi
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.DateUtil
import com.hazrat.islam24.util.DateUtil.dateLongToString
import com.hazrat.islam24.util.DateUtil.getCurrentDate
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-12-2024
 */

class PrayerTimeRepositoryImpl(
    private val api: PrayerTimeApi,
    private val locationRepository: LocationRepositoryImpl,
    private val prayerSettingRepository: PrayerSettingRepository,
    private val prayerTimeDao: PrayerTimeDao,
    private val context: Context,
    private val networkRepository: NetworkRepository
) : PrayerTimeRepository {

    private val networkStatus: StateFlow<ConnectivityObserver.Status> =
        networkRepository.networkStatus
    private val _prayerTimes = MutableStateFlow<List<PrayerTimeEntity>>(emptyList())
    override val prayerTimes = _prayerTimes.asStateFlow()

    private val _prayerTimeByDate = MutableStateFlow<List<PrayerTimeEntity>>(emptyList())
    override val prayerTimeByDate = _prayerTimeByDate.asStateFlow()

    override suspend fun newPrayerTimesRequest(): List<PrayerTimeEntity> {
        return try {
            val location: LocationEntity? = locationRepository.getLocation()
            val latitude = location?.latitude ?: 21.422487
            val longitude = location?.longitude ?: 39.826206
            val methodList = prayerSettingRepository.getCalculationMethod().firstOrNull()
            val juristicList = prayerSettingRepository.getJuristicMethod().firstOrNull()
            val methodValue = methodList?.method ?: 1
            val schoolValue = juristicList?.school ?: 0

            val year = DateUtil.getCurrentYear()
            val month = DateUtil.getCurrentMonth()
            if (networkStatus.value != ConnectivityObserver.Status.Available) {
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
            prayerTimeDao.insertAllPrayerTimes(apiResponse.data.values.flatten().toEntityList())
            apiResponse.data.values.flatten().toEntityList()
        } catch (e: HttpException) {
            Log.e("NewPrayerTimeRepositoryImpl", "HTTP Error: ${e.code()} - ${e.message()}")
            throw e
        } catch (e: Exception) {
            Log.e("NewPrayerTimeRepositoryImpl", "Unexpected Error: ${e.message}")
            throw e
        }
    }


    override fun getAllPrayer(): Flow<List<PrayerTimeEntity>> =
        prayerTimeDao.getAllPrayer().flowOn(Dispatchers.IO)
            .conflate()

    override fun getPrayerTimeByDate(): Flow<List<PrayerTimeEntity>> {
        val today = getCurrentDate() // Utility function to get the current date as a String
        return prayerTimeDao.getPrayerTimesFromDate(today) // Fetch the list of prayer times from DAO
            .distinctUntilChanged() // Avoid unnecessary recomputations when data hasn't changed
            .map { prayerTimes ->
                // Optionally update shared state with the list, if needed
                _prayerTimeByDate.value = prayerTimes
                Log.d("NewPrayerTimeRepositoryImpl", "Prayer times for today: $prayerTimes")
                prayerTimes // Emit the list of prayer times
            }
    }


    override fun sharePrayerTimes(prayerTimes: List<PrayerTimeEntity>) {
        val today = getCurrentDay() - 1
        val prayerTimeIndex = prayerTimes[today]
        val text =
            "Today's prayer times\n ${prayerTimeIndex.gregorianDate}// ${prayerTimeIndex.hijriDate}\n\n" +
                    "${context.getString(R.string.fajr)}: ${dateLongToString(prayerTimeIndex.fajrTime)}\n" +
                    "${context.getString(R.string.dhuhr)}: ${dateLongToString(prayerTimeIndex.dhuhrTime)}\n" +
                    "${context.getString(R.string.asr)}: ${dateLongToString(prayerTimeIndex.asrTime)}\n" +
                    "${context.getString(R.string.maghrib)}: ${dateLongToString(prayerTimeIndex.maghribTime)}\n" +
                    "${context.getString(R.string.isha_a)}: ${dateLongToString(prayerTimeIndex.ishaTime)}\n\n" +
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
            .collectLatest { prayerList: List<PrayerTimeEntity> ->
                val currentYear = DateUtil.getCurrentYear()
                if (prayerList.isEmpty() || currentYear != prayerList[0].gregorianYear.toInt()) {
                    if (networkStatus.value == ConnectivityObserver.Status.Available) {
                        newPrayerTimesRequest()
                    }
                } else {
                    _prayerTimes.value = prayerList
                }
            }
    }

    override fun getHijriDay(): Int {
        val currentGregorianDay = getCurrentDate()

        val hijriDateEntity = _prayerTimes.value.indexOfFirst {
            it.gregorianDate == currentGregorianDay
        }
        val hijriDate = _prayerTimes.value[hijriDateEntity].hijriDay
        return hijriDate
    }
}