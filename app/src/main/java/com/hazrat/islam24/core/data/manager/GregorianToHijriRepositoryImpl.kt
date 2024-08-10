package com.hazrat.islam24.core.data.manager

import com.hazrat.islam24.core.data.dao.GregorianToHijriDao
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.core.domain.model.gregoriantohijri.GregorianToHijriResponse
import com.hazrat.islam24.core.domain.repository.GregorianToHijriRepository
import com.hazrat.islam24.core.network.GregorianToHijriApi
import com.hazrat.islam24.util.DateUtil
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the GregorianToHijriRepository interface.
 * This class handles the retrieval of Gregorian to Hijri dates from the API and database.
 *
 * @param api The API service for retrieving Gregorian to Hijri dates.
 * @param gregorianToHijriDao The Data Access Object (DAO) for interacting with the local database.
 */
class GregorianToHijriRepositoryImpl(
    private val api: GregorianToHijriApi,
    private val gregorianToHijriDao: GregorianToHijriDao
): GregorianToHijriRepository {

    /**
     * Retrieves the current Gregorian to Hijri date from the API.
     *
     * @return The GregorianToHijriResponse containing the current Hijri date.
     * @throws Exception if an error occurs during the API call or data processing.
     */
    override suspend fun getGregorianToHijriDate(): GregorianToHijriResponse {
        val date = DateUtil.getCurrentDate()
        try {
            val response = api.getGtoHDate(date)
            if (response.code == 200 && response.status == "OK") {
                saveResponseToDatabase(response)
                return response
            } else {
                // Handle error cases appropriately
                throw Exception("Failed to get Hijri date: ${response.status}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Retrieves the Gregorian to Hijri date entities from the local database.
     *
     * @return Flow representing the list of GregorianToHijriEntity objects.
     */
    override fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>> {
        return gregorianToHijriDao.getGregorianToHijriData()
    }

    /**
     * Saves the received Gregorian to Hijri date response to the local database.
     *
     * @param response The GregorianToHijriResponse containing the Hijri date to be saved.
     */
    private suspend fun saveResponseToDatabase(response: GregorianToHijriResponse) {
        val data = response.data.hijri
        val hijriEntity = GregorianToHijriEntity(
            date = data.date,
            day = data.day.toInt(),
            weekdayEn = data.weekday.en,
            weekDayAr = data.weekday.ar,
            monthNumber = data.month.number,
            monthAr = data.month.ar,
            monthEn = data.month.en,
            year = data.year,
            designationAbbreviated = data.designation.abbreviated,
            designationExpanded = data.designation.expanded
        )
        gregorianToHijriDao.insertOrUpdate(hijriEntity)
    }
}


