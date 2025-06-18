package com.hazrat.islam24.core.domain.repository.prayertime

/**
 * @author Hazrat Ummar Shaikh
 */

import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PrayerTimeRepository {

    val prayerTimes: StateFlow<List<PrayerTimeEntity>>
    val prayerTimeByDate: StateFlow<List<PrayerTimeEntity>>
    /**
     * Fetches and saves prayer times for the current month.
     * This function is responsible for retrieving prayer times from an external source and storing them in the database.
     *
     * @return A list of PrayerTimeEntity objects representing the prayer times for the month.
     */

    suspend fun newPrayerTimesRequest(): List<PrayerTimeEntity>


    /**
     * Retrieves all prayer times from the database.
     *
     * @return A Flow representing a list of PrayerTimeEntity objects.
     */
    fun getAllPrayer(): Flow<List<PrayerTimeEntity>>

    fun getPrayerTimeByDate(): Flow<List<PrayerTimeEntity>>


    fun sharePrayerTimes(prayerTimes: List<PrayerTimeEntity>)

    suspend fun getAllPrayerTimes()

    fun getHijriDay(): Int

}
