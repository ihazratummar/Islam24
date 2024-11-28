package com.hazrat.islam24.core.domain.repository.prayertime

/**
 * @author Hazrat Ummar Shaikh
 */

import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PrayerTimeRepository {

    val prayerTimes: StateFlow<List<PrayerTimeEntity>>
    /**
     * Fetches and saves prayer times for the current month.
     * This function is responsible for retrieving prayer times from an external source and storing them in the database.
     *
     * @return A list of PrayerTimeEntity objects representing the prayer times for the month.
     */
    suspend fun fetchAndSavePrayerTimesForMonth(): List<PrayerTimeEntity>

    /**
     * Inserts a list of prayer times into the database.
     * If a prayer time with the same primary key already exists, it will be replaced.
     *
     * @param prayerTimes A list of PrayerTimeEntity objects to be inserted.
     * @return The list of PrayerTimeEntity objects that were inserted.
     */
    suspend fun insertAllPrayerTimes(prayerTimes: List<PrayerTimeEntity>): List<PrayerTimeEntity>

    /**
     * Retrieves all prayer times from the database.
     *
     * @return A Flow representing a list of PrayerTimeEntity objects.
     */
    fun getAllPrayer(): Flow<List<PrayerTimeEntity>>

    /**
     * Deletes specific prayer times from the database.
     *
     * @param prayerTimeEntity A list of PrayerTimeEntity objects to be deleted.
     */
    suspend fun deletePrayerTime(prayerTimeEntity: List<PrayerTimeEntity>)

    /**
     * Deletes all prayer times from the database.
     */
    suspend fun deleteAllPrayer()

    fun sharePrayerTimes(prayerTimes: List<PrayerTimeEntity>)

    suspend fun getAllPrayerTimes()

}
