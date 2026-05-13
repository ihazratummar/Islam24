package com.hazrat.prayertime.data.repository

import android.content.Context
import android.content.Intent
import com.hazrat.database.dao.PrayerTimeDao
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
        val currentYear = DateUtil.getCurrentYear()
        val currentMonth = DateUtil.getCurrentMonth()
        
        // Fetch current year
        val entities = fetchAndSaveYearlyPrayerTimes(currentYear, currentMonth)
        
        // If we are in December, also pre-fetch next year
        if (currentMonth == 12) {
            try {
                fetchAndSaveYearlyPrayerTimes(currentYear + 1, 1)
            } catch (e: Exception) {
                Timber.tag("NewPrayerTimeRepositoryImpl").e("Failed to pre-fetch next year: ${e.message}")
            }
        }
        
        return entities
    }

    private suspend fun fetchAndSaveYearlyPrayerTimes(year: Int, month: Int): List<PrayerTimeEntity> {
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
            
            val methodList = prayerSettingRepository.getCalculationMethod().firstOrNull()
            val juristicList = prayerSettingRepository.getJuristicMethod().firstOrNull()
            val methodValue = methodList?.method ?: 1
            val schoolValue = juristicList?.school ?: 0

            val networkStatus = connectivityObserver.observer().first()
            if (networkStatus != ConnectivityObserver.Status.Available) {
                throw IOException("No network available to fetch prayer times for $year.")
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

    override fun getPrayerTimeByDate(): Flow<List<PrayerTimeModel>> {
        val today = DateUtil.getCurrentDate() 
        return prayerTimeDao.getPrayerTimesFromDate(today) 
            .distinctUntilChanged()
            .map { prayerTimes ->
                val modelList = prayerTimes.toPrayerModelList()
                _prayerTimeByDate.value = modelList
                modelList
            }
    }


    override fun sharePrayerTimes(prayerTimes: List<PrayerTimeModel>) {
        // Find today's prayer time in the list by matching Gregorian date
        val today = DateUtil.getCurrentDate()
        val prayerTimeIndex = prayerTimes.find { it.gregorianDate == today } ?: return
        
        val text =
            "Today's prayer times\n ${prayerTimeIndex.gregorianDate}// ${prayerTimeIndex.hijriDate}\n\n" +
                    "${context.getString(R.string.fajr)}: ${DateUtil.dateLongToString(prayerTimeIndex.fajrTime)}\n" +
                    "${context.getString(R.string.dhuhr)}: ${DateUtil.dateLongToString(prayerTimeIndex.dhuhrTime)}\n" +
                    "${context.getString(R.string.asr)}: ${DateUtil.dateLongToString(prayerTimeIndex.asrTime)}\n" +
                    "${context.getString(R.string.maghrib)}: ${DateUtil.dateLongToString(prayerTimeIndex.maghribTime)}\n" +
                    "${context.getString(R.string.isha_a)}: ${DateUtil.dateLongToString(prayerTimeIndex.ishaTime)}\n\n" +
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
                    val currentMonth = DateUtil.getCurrentMonth()
                    if (currentMonth == 12) {
                        val hasNextYearData = prayerList.any { it.gregorianYear.toInt() == currentYear + 1 }
                        if (!hasNextYearData) {
                            val networkStatus = connectivityObserver.observer().first()
                            if (networkStatus == ConnectivityObserver.Status.Available) {
                                try {
                                    fetchAndSaveYearlyPrayerTimes(currentYear + 1, 1)
                                } catch (e: Exception) {
                                    Timber.tag("NewPrayerTimeRepositoryImpl").e("Auto pre-fetch failed: ${e.message}")
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
