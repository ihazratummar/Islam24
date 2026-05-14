package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hazrat.database.entity.PrayerTimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerTimeDao {

    /**
     * Inserts or updates a list of prayer times into the database.
     * If a prayer time with the same primary key already exists, it will be replaced.
     *
     * @param prayerTime The list of PrayerTimeEntity objects to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPrayerTimes(prayerTime: List<PrayerTimeEntity>)

    /**
     * Retrieves all prayer times from the database.
     *
     * @return Flow representing the list of prayer times as PrayerTimeEntity objects.
     */
    @Query("SELECT * FROM prayer_times")
    fun getAllPrayer(): Flow<List<PrayerTimeEntity>>

    /**
     * Retrieves the prayer time for a specific day from the database.
     *
     * @param day The day for which prayer time is requested.
     * @return The prayer time for the specified day as a PrayerTimeEntity object, or null if not found.
     */
    @Query("SELECT * FROM prayer_times WHERE GregorianDate >= :currentDate ORDER BY GregorianDate ASC")
    fun getPrayerTimesFromDate(currentDate: String): Flow<List<PrayerTimeEntity>>


    /**
     *
     */

    @Query("SELECT * FROM prayer_times WHERE GregorianDate == :currentDate LIMIT 1")
    fun getPrayerTimeForToday(currentDate: String) : Flow<PrayerTimeEntity>


    /**
     * Deletes specific prayer times from the database.
     *
     * @param prayerTime The list of PrayerTimeEntity objects to be deleted.
     */
    @Delete
    fun deletePrayerTime(prayerTime: List<PrayerTimeEntity>)

    /**
     * Deletes all prayer times from the database.
     * This method is useful for cleaning up the database.
     */
    @Query("DELETE FROM prayer_times")
    suspend fun deleteAllPrayer()

    /**
     * Updates a prayer time in the database.
     *
     * @param prayerTime The updated PrayerTimeEntity object.
     */
    @Update
    suspend fun updatePrayerTime(prayerTime: PrayerTimeEntity)


    @Query("SELECT `Fajr Time` FROM prayer_times WHERE GregorianDate == :currentDate")
    fun getFajrTimeForTheDay(currentDate: String) : Long

    @Query("SELECT `Dhuhr Time` FROM prayer_times WHERE GregorianDate == :currentDate ")
    fun getDhuhrTimeForTheDay(currentDate: String) : Long

    @Query("SELECT AsrTime FROM prayer_times WHERE GregorianDate == :currentDate")
    fun getAsrTimeForTheDay(currentDate: String) : Long

    @Query("SELECT `Maghrib Time` FROM prayer_times WHERE GregorianDate == :currentDate ")
    fun getMaghribTimeForTheDay(currentDate: String) : Long

    @Query("SELECT `Isha Time` FROM prayer_times WHERE GregorianDate == :currentDate ")
    fun getIshaTimeForTheDay(currentDate: String) : Long
}