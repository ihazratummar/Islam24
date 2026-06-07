package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.HijriCalendarEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HijriCalendarDao {

    /**
     * Inserts or updates the HijriCalendarEntity in the database.
     * If the entity already exists, it will be replaced.
     *
     * @param hijriCalendarEntity The HijriCalendarEntity to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHijriCalendar(hijriCalendarEntity: HijriCalendarEntity)

    /**
     * Retrieves a list of all Hijri calendar entries from the database.
     * Emits the result as a Flow of List of HijriCalendarEntity.
     *
     * @return Flow representing the list of all Hijri calendar entries.
     */
    @Query("SELECT * FROM hijricalendarentity")
    fun getCalendarList(): Flow<List<HijriCalendarEntity>>

    /**
     * Retrieves Hijri calendar data for a specific Gregorian date from the database.
     * Emits the result as a Flow of List of HijriCalendarEntity.
     *
     * @param gregorianDate The Gregorian date for which Hijri calendar data is requested.
     * @return Flow representing the Hijri calendar data for the specified Gregorian date.
     */
    @Query("SELECT * FROM hijricalendarentity WHERE gregorianDate = :gregorianDate")
    fun getHijriCalendarForDate(gregorianDate: String): Flow<List<HijriCalendarEntity>>

    /**
     * Retrieves Hijri calendar data for a specific Gregorian date as a Flow.
     * This method converts the given LocalDate to a string and then calls [getHijriCalendarForDate] method.
     *
     * @param gregorianDate The Gregorian date for which Hijri calendar data is requested, as LocalDate.
     * @return Flow representing the Hijri calendar data for the specified Gregorian date.
     */
    fun getHijriCalendarForDateAsFlow(gregorianDate: LocalDate): Flow<List<HijriCalendarEntity>> {
        val dateString = gregorianDate.toString()
        return getHijriCalendarForDate(dateString)
    }

}