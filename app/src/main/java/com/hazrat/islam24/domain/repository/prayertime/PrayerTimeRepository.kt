//PrayerTimeRepository.kt

package com.hazrat.islam24.domain.repository.prayertime


import android.util.Log
import com.hazrat.islam24.data.prayertime.PrayerTimeDao
import com.hazrat.islam24.data.prayertime.PrayerTimeEntity
import com.hazrat.islam24.network.PrayerTimeApi
import com.hazrat.islam24.util.DateUtil
import com.hazrat.islam24.data.location.coordinents.LocationEntity
import com.hazrat.islam24.domain.model.prayertime.prayertimemodel.ApiResponse
import com.hazrat.islam24.domain.model.prayertime.prayertimemodel.Data
import com.hazrat.islam24.domain.repository.location.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PrayerTimeRepository @Inject constructor(
    private val api: PrayerTimeApi,
    private val locationRepository: LocationRepository,
    private val prayerSettingRepository: PrayerSettingRepository,
    private val prayerTimeDao: PrayerTimeDao

){



    private suspend fun getApiParameterForMonth(): ApiResponse {
        val location: LocationEntity? = locationRepository.getLocation()
        val latitude = location?.latitude?: 24.628
        val longitude = location?.longitude?: 88.011
        val methodList = prayerSettingRepository.getMethod().firstOrNull() ?: emptyList()
        val methodValue = methodList.firstOrNull()?.method?:1 // Default value is 1 if methodList or method is null
        val schoolValue = methodList.firstOrNull()?.school?:0
        Log.d("ChekingApi","$latitude $longitude $methodValue $schoolValue")
        val year = DateUtil.getCurrentYear()
        val month = DateUtil.getCurrentMonth()
        val apiResponse = api.getPrayerTimes(year, month, "$latitude", "$longitude", methodValue, schoolValue)
        apiResponse.data.forEach { apiDataForDay ->
            val prayerTimeEntity = convertApiResponseToEntity(apiDataForDay)
            updatePrayerTime(prayerTimeEntity)
        }
        Log.d("PrayerTimeRepository", "API response: $apiResponse")
        return apiResponse
    }


    private fun convertApiResponseToEntity(apiResponse: Data): PrayerTimeEntity {
        val timings = apiResponse.timings
        val date = apiResponse.date
        val meta = apiResponse.meta
        return PrayerTimeEntity(
            day = date.gregorian.day.toInt(),
            fajrTime = timings.Fajr,
            sunriseTime = timings.Sunrise,
            dhuhrTime = timings.Dhuhr,
            asrTime = timings.Asr,
            sunsetTime = timings.Sunset,
            maghribTime = timings.Maghrib,
            ishaTime = timings.Isha,
            imsakTime = timings.Imsak,
            midnightTime = timings.Midnight,
            firstThirdTime = timings.Firstthird,
            lastThirdTime = timings.Lastthird,
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

    suspend fun fetchAndSavePrayerTimesForMonth(): List<PrayerTimeEntity> {
        val apiResponse = getApiParameterForMonth()
        val prayerTimesList = mutableListOf<PrayerTimeEntity>()
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
        Log.d("Insertion", "Prayer time entities inserted successfully")
        return prayerTimesList
    }


    suspend fun insertAllPrayerTimes(prayerTimes: List<PrayerTimeEntity>): List<PrayerTimeEntity> {
        prayerTimeDao.insertAllPrayerTimes(prayerTimes)
        return prayerTimes
    }
    fun getAllPrayer(): Flow<List<PrayerTimeEntity>> = prayerTimeDao.getAllPrayer().flowOn(Dispatchers.IO)
        .conflate()
    suspend fun deletePrayerTime(prayerTimeEntity: List<PrayerTimeEntity>) = prayerTimeDao.deletePrayerTime(prayerTimeEntity)

    suspend fun deleteAllPrayer() = prayerTimeDao.deleteAllPrayer()
    private suspend fun updatePrayerTime(prayerTime: PrayerTimeEntity) = prayerTimeDao.updatePrayerTime(prayerTime)


}