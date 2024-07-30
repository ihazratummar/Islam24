//PrayerTimeRepositoryImpl.kt

package com.hazrat.islam24.core.data.manager


import android.util.Log
import com.hazrat.islam24.core.data.dao.PrayerTimeDao
import com.hazrat.islam24.core.data.entity.LocationEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.network.PrayerTimeApi
import com.hazrat.islam24.core.data.manager.LocationRepositoryImpl
import com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Data
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.util.DateUtil
import com.hazrat.islam24.util.DateUtil.timeStringToLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PrayerTimeRepositoryImpl @Inject constructor(
    private val api: PrayerTimeApi,
    private val locationRepository: LocationRepositoryImpl,
    private val prayerSettingRepository: PrayerSettingRepository,
    private val prayerTimeDao: PrayerTimeDao

): PrayerTimeRepository {



    private suspend fun getApiParameterForMonth(): com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.ApiResponse? {
        return try {
            val location: LocationEntity? = locationRepository.getLocation()
            val latitude = location?.latitude ?: 24.628
            val longitude = location?.longitude ?: 88.011
            val methodList = prayerSettingRepository.getCalculationMethod().firstOrNull()
            val juristicList = prayerSettingRepository.getJuristicMethod().firstOrNull()
            val methodValue = methodList?.method?:1 // Default value is 1 if methodList or method is null
            val schoolValue = juristicList?.school?:0

            val year = DateUtil.getCurrentYear()
            val month = DateUtil.getCurrentMonth()

            val apiResponse = api.getPrayerTimes(year, month, "$latitude", "$longitude", methodValue, schoolValue)
            apiResponse.data.forEach { apiDataForDay ->
                val prayerTimeEntity = convertApiResponseToEntity(apiDataForDay)
                updatePrayerTime(prayerTimeEntity)
            }
            apiResponse
        } catch (e: HttpException) {
            Log.e("ApiError", "HTTP error: ${e.code()}", e)
            null
        } catch (e: IOException) {
            Log.e("ApiError", "Network error", e)
            null
        } catch (e: Exception) {
            Log.e("ApiError", "Unknown error", e)
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
            gregorianMonthName =date.gregorian.month.en,
            gregorianYear = date.gregorian.year,
            hijriDate = date.hijri.date,
            hijriDay =date.hijri.day,
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

        ).apply {
            Log.d("PrayerTimeRepository", "Converted entity: $this")
        }

    }

    override suspend fun fetchAndSavePrayerTimesForMonth(): List<PrayerTimeEntity> {
        val apiResponse = getApiParameterForMonth()
        val prayerTimesList = mutableListOf<PrayerTimeEntity>()
        if (apiResponse != null) {
            for (apiDataForDay in apiResponse.data) {
                val prayerTimeEntity = convertApiResponseToEntity(apiDataForDay)

                // Check if the entity for this day already exists in the database
                val existingEntity = prayerTimeDao.getPrayerTimeByDay(prayerTimeEntity.day)
                if (existingEntity == null) {
                    // If it doesn't exist, insert the entity into the database
                    prayerTimeDao.insertAllPrayerTimes(listOf(prayerTimeEntity))
                    Log.d("Insertion", "Inserting prayer time entity: $prayerTimeEntity")
                    prayerTimesList.add(prayerTimeEntity)
                } else {
                    // If it already exists, you may want to handle this case accordingly
                    Log.d("Insertion", "Prayer time entity for day ${prayerTimeEntity.day} already exists")
                }
            }
        }
        Log.d("Insertion", "Prayer time entities inserted successfully")
        return prayerTimesList
    }


    override suspend fun insertAllPrayerTimes(prayerTimes: List<PrayerTimeEntity>): List<PrayerTimeEntity> {
        prayerTimeDao.insertAllPrayerTimes(prayerTimes)
        return prayerTimes
    }
    override fun getAllPrayer(): Flow<List<PrayerTimeEntity>> = prayerTimeDao.getAllPrayer().flowOn(Dispatchers.IO)
        .conflate()
    override suspend fun deletePrayerTime(prayerTimeEntity: List<PrayerTimeEntity>) = prayerTimeDao.deletePrayerTime(prayerTimeEntity)

    override suspend fun deleteAllPrayer() = prayerTimeDao.deleteAllPrayer()
    private suspend fun updatePrayerTime(prayerTime: PrayerTimeEntity) = prayerTimeDao.updatePrayerTime(prayerTime)


}