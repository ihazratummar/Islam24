package com.hazrat.domain.repository

import com.hazrat.database.entity.PrayerTimeEntity
import com.hazrat.model.PrayerTimeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Hazrat Ummar Shaikh
 */

interface PrayerTimeRepository {

    val prayerTimes: StateFlow<List<PrayerTimeModel>>
    val prayerTimeByDate: StateFlow<List<PrayerTimeModel>>
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
    fun getAllPrayer(): Flow<List<PrayerTimeModel>>

    fun getPrayerTimeByDate(): Flow<List<PrayerTimeModel>>


    fun sharePrayerTimes(prayerTimes: List<PrayerTimeModel>)

    suspend fun getAllPrayerTimes()

    fun getHijriDay(): Int

}