//PrayerTimeRepositoryImpl.kt

package com.hazrat.islam24.core.data.repository


import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import com.hazrat.islam24.R
import com.hazrat.islam24.core.api.PrayerTimeApi
import com.hazrat.islam24.core.data.dao.PrayerTimeDao
import com.hazrat.islam24.core.data.entity.LocationEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.ApiResponse
import com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Data
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.DateUtil
import com.hazrat.islam24.util.DateUtil.dateLongToString
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import com.hazrat.islam24.util.DateUtil.timeStringToLong
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
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PrayerTimeRepositoryImpl @Inject constructor(
    private val api: PrayerTimeApi,
    private val locationRepository: LocationRepositoryImpl,
    private val prayerSettingRepository: PrayerSettingRepository,
    private val prayerTimeDao: PrayerTimeDao,
    private val context: Context,
    private val networkRepository: NetworkRepository
) : PrayerTimeRepository {

    private val networkStatus: StateFlow<ConnectivityObserver.Status> = networkRepository.networkStatus
    private val _prayerTimes = MutableStateFlow<List<PrayerTimeEntity>>(emptyList())
    override val prayerTimes = _prayerTimes.asStateFlow()

    private suspend fun getApiParameterForMonth(): ApiResponse? {
        return try {
            val location: LocationEntity? = locationRepository.getLocation()
            val latitude = location?.latitude ?: 24.628
            val longitude = location?.longitude ?: 88.011
            val methodList = prayerSettingRepository.getCalculationMethod().firstOrNull()
            val juristicList = prayerSettingRepository.getJuristicMethod().firstOrNull()
            val methodValue = methodList?.method ?: 1
            val schoolValue = juristicList?.school ?: 0

            val year = DateUtil.getCurrentYear()
            val month = DateUtil.getCurrentMonth()

            if (_networkStatus.value == ConnectivityObserver.Status.Available){
                val apiResponse =
                    api.getPrayerTimes(year, month, "$latitude", "$longitude", methodValue, schoolValue)
                apiResponse.data.forEach { apiDataForDay ->
                    val prayerTimeEntity = convertApiResponseToEntity(apiDataForDay)
                    updatePrayerTime(prayerTimeEntity)
                }
                apiResponse
            }else{
                null
            }
        } catch (e: HttpException) {
            null
        } catch (e: IOException) {
            null
        } catch (e: Exception) {
            null
        }
    }


    private fun convertApiResponseToEntity(apiResponse: Data): PrayerTimeEntity {
        val timings = apiResponse.timings
        val date = apiResponse.date
        val meta = apiResponse.meta
        return PrayerTimeEntity(
            day = date.gregorian.day.toInt(),
            fajrTime = timeStringToLong("${date.gregorian.date} ${timings.Fajr}"),
            sunriseTime = timeStringToLong("${date.gregorian.date} ${timings.Sunrise}"),
            dhuhrTime = timeStringToLong("${date.gregorian.date} ${timings.Dhuhr}"),
            asrTime = timeStringToLong("${date.gregorian.date} ${timings.Asr}"),
            sunsetTime = timeStringToLong("${date.gregorian.date} ${timings.Sunset}"),
            maghribTime = timeStringToLong("${date.gregorian.date} ${timings.Maghrib}"),
            ishaTime = timeStringToLong("${date.gregorian.date} ${timings.Isha}"),
            imsakTime = timeStringToLong("${date.gregorian.date} ${timings.Imsak}"),
            midnightTime = timeStringToLong("${date.gregorian.date} ${timings.Midnight}"),
            firstThirdTime = timeStringToLong("${date.gregorian.date} ${timings.Firstthird}"),
            lastThirdTime = timeStringToLong("${date.gregorian.date} ${timings.Lastthird}"),
            readableDate = date.readable,
            gregorianDate = date.gregorian.date,
            gregorianDay = date.gregorian.day,
            gregorianWeekday = date.gregorian.weekday.en,
            gregorianMonthNum = date.gregorian.month.number,
            gregorianMonthName = date.gregorian.month.en,
            gregorianYear = date.gregorian.year,
            hijriDate = date.hijri.date,
            hijriDay = date.hijri.day,
            hijriWeekdayEn = date.hijri.weekday.en,
            hijriWeekdayEr = date.hijri.weekday.ar,
            hijriMonthAr = date.hijri.month.ar,
            hijriMonthEn = date.hijri.month.en,
            hijriMonthNumber = date.hijri.month.number,
            hijriYear = date.hijri.year,
            hijriab = date.hijri.designation.abbreviated,
            timezone = meta.timezone,
            methodId = meta.method.id,
            methodName = meta.method.name,
            methodFajrParam = meta.method.params.Fajr,
            methodIshaParam = meta.method.params.Isha,
            latitudeAdjustmentMethod = meta.latitudeAdjustmentMethod,
            midnightMode = meta.midnightMode,
            school = meta.school
        )
    }

    override suspend fun fetchAndSavePrayerTimesForMonth(): List<PrayerTimeEntity> {
        val apiResponse = getApiParameterForMonth()
        val prayerTimesList = mutableListOf<PrayerTimeEntity>()
        if (apiResponse != null) {
            for (apiDataForDay in apiResponse.data) {
                val prayerTimeEntity = convertApiResponseToEntity(apiDataForDay)
                val existingEntity = prayerTimeDao.getPrayerTimeByDay(prayerTimeEntity.day)
                if (existingEntity == null) {
                    prayerTimeDao.insertAllPrayerTimes(listOf(prayerTimeEntity))
                    prayerTimesList.add(prayerTimeEntity)
                }
            }
        }
        return prayerTimesList
    }


    override suspend fun insertAllPrayerTimes(prayerTimes: List<PrayerTimeEntity>): List<PrayerTimeEntity> {
        prayerTimeDao.insertAllPrayerTimes(prayerTimes)
        return prayerTimes
    }

    override fun getAllPrayer(): Flow<List<PrayerTimeEntity>> =
        prayerTimeDao.getAllPrayer().flowOn(Dispatchers.IO)
            .conflate()

    override suspend fun deletePrayerTime(prayerTimeEntity: List<PrayerTimeEntity>) =
        prayerTimeDao.deletePrayerTime(prayerTimeEntity)

    override suspend fun deleteAllPrayer() = prayerTimeDao.deleteAllPrayer()
    private suspend fun updatePrayerTime(prayerTime: PrayerTimeEntity) =
        prayerTimeDao.updatePrayerTime(prayerTime)

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
                if (prayerList.isEmpty()) {
                    if (networkStatus.value == ConnectivityObserver.Status.Available){
                        getApiParameterForMonth()
                    }
                } else {
                    _prayerTimes.value = prayerList
                }
            }
    }

}
