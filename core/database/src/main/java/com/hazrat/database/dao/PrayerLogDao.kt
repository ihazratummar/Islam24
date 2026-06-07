package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.PrayerLogEntity
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 18/05/26
 */

@Dao
interface PrayerLogDao {

    // ── WRITES ────────────────────────────────────────────────────────────────

    /**
     * UPSERT: OnConflictStrategy.REPLACE
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLog(entity: PrayerLogEntity)


    @Query("DELETE FROM prayer_logs WHERE date = :date AND prayer = :prayer")
    suspend fun hardDelete(date: String, prayer: String)

    @Query("SELECT * FROM prayer_logs WHERE date = :date AND is_deleted = 0 ORDER BY prayer ASC")
    fun observeDailyLogs(date: String) : Flow<List<PrayerLogEntity>>




}