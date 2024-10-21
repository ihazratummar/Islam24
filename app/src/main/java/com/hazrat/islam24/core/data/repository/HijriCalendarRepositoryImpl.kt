package com.hazrat.islam24.core.data.repository

import com.hazrat.islam24.core.data.dao.GregorianToHijriDao
import com.hazrat.islam24.core.data.dao.HijriCalendarDao
import com.hazrat.islam24.core.domain.model.hijricalendar.Data
import com.hazrat.islam24.core.domain.model.hijricalendar.HijriCalendarResponse
import com.hazrat.islam24.core.domain.repository.HijriCalendarRepository
import com.hazrat.islam24.core.api.HijriCalendarApi
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.core.data.entity.HijriCalendarEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn

/**
 * Implementation of the HijriCalendarRepository interface.
 * This class handles the retrieval and storage of Hijri calendar data from the API and local database.
 *
 * @param api The API service for retrieving Hijri calendar data.
 * @param gregorianToHijriDao The Data Access Object (DAO) for accessing Gregorian to Hijri conversion data.
 * @param hijriCalendarDao The Data Access Object (DAO) for accessing Hijri calendar data.
 */
class HijriCalendarRepositoryImpl(
    private val api: HijriCalendarApi,
    private val gregorianToHijriDao: GregorianToHijriDao,
    private val hijriCalendarDao: HijriCalendarDao
) : HijriCalendarRepository {

    /**
     * Retrieves the Hijri calendar data from the API, based on the converted Gregorian date.
     *
     * @return The HijriCalendarResponse containing the retrieved Hijri calendar data.
     * @throws IllegalStateException if the GregorianToHijriEntity list is null or empty.
     */
    override suspend fun getHijriCalendarFromApi(): HijriCalendarResponse {
        val dataList: List<GregorianToHijriEntity> = gregorianToHijriEntity().firstOrNull()
            ?: throw IllegalStateException("GregorianToHijriEntity list is null or empty")

        if (dataList.isNotEmpty()) {
            val singleData = dataList.first()
            val month = singleData.monthNumber
            val year = singleData.year

            val apiResponse = api.getHijriCalendar(month, year)
            apiResponse.data.forEach { apiDates ->
                val hijriCalendarEntity = convertApiResponseToEntity(apiDates)
                insertCalendarList(hijriCalendarEntity)
            }
            return apiResponse
        } else {
            // Handle the case when GregorianToHijriEntity list is empty
            throw IllegalStateException("GregorianToHijriEntity list is empty")
        }
    }

    /**
     * Retrieves the Gregorian to Hijri conversion data entities from the local database.
     *
     * @return Flow representing the list of GregorianToHijriEntity objects.
     */
    override fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>> =
        gregorianToHijriDao.getGregorianToHijriData().flowOn(Dispatchers.IO)
            .conflate()

    /**
     * Inserts the provided list of Hijri calendar entities into the local database.
     *
     * @param hijriCalendarList The list of HijriCalendarEntity objects to be inserted.
     * @return The inserted Hijri calendar entity.
     */
    override suspend fun insertCalendarList(hijriCalendarList: HijriCalendarEntity): HijriCalendarEntity {
        hijriCalendarDao.insertHijriCalendar(hijriCalendarList)
        return hijriCalendarList
    }

    /**
     * Retrieves the list of Hijri calendar entities from the local database.
     *
     * @return Flow representing the list of HijriCalendarEntity objects.
     */
    override fun getCalendarList(): Flow<List<HijriCalendarEntity>> =
        hijriCalendarDao.getCalendarList().flowOn(Dispatchers.Default)
            .conflate()

    /**
     * Converts the API response to a HijriCalendarEntity object.
     *
     * @param apiResponse The response from the API containing Hijri calendar data.
     * @return The HijriCalendarEntity object representing the converted API response.
     */
    private fun convertApiResponseToEntity(apiResponse: Data): HijriCalendarEntity {
        val hijri = apiResponse.hijri
        val gregorian = apiResponse.gregorian
        return HijriCalendarEntity(
            hijriDay = hijri.day.toInt(),
            gregorianDay = gregorian.day.toInt(),
            hijriDate = hijri.date,
            gregorianDate = gregorian.date,
            hijriYear = hijri.year,
            hijriAbbreviated = hijri.designation.abbreviated,
            gregorianYear = gregorian.year,
            gregorianAbbreviated = gregorian.designation.abbreviated,
            hijriMonthNumber = hijri.month.number,
            hijriMonthEn = hijri.month.en,
            hijriMonthAr = hijri.month.ar,
            gregorianMonthNumber = gregorian.month.number,
            gregorianMonthEn = gregorian.month.en,
            hijriWeekDayEn = hijri.weekday.en,
            hijriWeekDayAr = hijri.weekday.ar,
            gregorianWeekDayEn = gregorian.weekday.en,
            holidays = hijri.holidays
        )
    }
}
