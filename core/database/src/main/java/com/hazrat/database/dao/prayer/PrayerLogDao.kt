package com.hazrat.database.dao.prayer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.prayer.PrayerLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

/**
 * @author hazratummar
 * Created on 18/05/26
 */

@Dao
interface PrayerLogDao {

    @Query("SELECT * FROM prayer_logs WHERE logDate = :date")
    fun observeLogByDate(date: LocalDate): Flow<PrayerLogEntity?>

    @Query("SELECT * FROM prayer_logs WHERE logDate = :date")
    suspend fun getLogByDate(date: LocalDate): PrayerLogEntity?

    @Query("UPDATE prayer_logs SET fajr = :fajr, isSynced = 0, updatedAt = :updatedAt WHERE logDate = :date")
    suspend fun updateFajr(date: LocalDate, fajr: Boolean, updatedAt: Instant)

    @Query("UPDATE prayer_logs SET dhuhr = :dhuhr, isSynced = 0, updatedAt = :updatedAt WHERE logDate = :date")
    suspend fun updateDhuhr(date: LocalDate, dhuhr: Boolean, updatedAt: Instant)

    @Query("UPDATE prayer_logs SET asr = :asr, isSynced = 0, updatedAt = :updatedAt WHERE logDate = :date")
    suspend fun updateAsr(date: LocalDate, asr: Boolean, updatedAt: Instant)

    @Query("UPDATE prayer_logs SET maghrib = :maghrib, isSynced = 0, updatedAt = :updatedAt WHERE logDate = :date")
    suspend fun updateMaghrib(date: LocalDate, maghrib: Boolean, updatedAt: Instant)

    @Query("UPDATE prayer_logs SET isha = :isha, isSynced = 0, updatedAt = :updatedAt WHERE logDate = :date")
    suspend fun updateIsha(date: LocalDate, isha: Boolean, updatedAt: Instant)

    @Query("SELECT * FROM prayer_logs WHERE logDate >= :startDate AND logDate <= :endDate")
    fun observeLogsBetween(startDate: LocalDate, endDate: LocalDate): Flow<List<PrayerLogEntity>>

    @Query("SELECT * FROM prayer_logs")
    fun observeAllLogs(): Flow<List<PrayerLogEntity>>

    @Query("SELECT COUNT(*) FROM prayer_logs")
    fun getTotalLoggedPrayersCount(): Flow<Int>

    @Query("SELECT * FROM prayer_logs ORDER BY logDate DESC")
    fun getAllLoggedEntities(): Flow<List<PrayerLogEntity>>

    @Query("SELECT * FROM prayer_logs WHERE isSynced = 0")
    suspend fun getUnsyncedLogs(): List<PrayerLogEntity>

    @Query("UPDATE prayer_logs SET isSynced = 1 WHERE logDate IN (:dates)")
    suspend fun markAsSynced(dates: List<LocalDate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(log: PrayerLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(logs: List<PrayerLogEntity>)

    @Query("DELETE FROM prayer_logs")
    suspend fun clearAll()
}