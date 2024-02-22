package com.hazrat.islam24.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hazrat.islam24.data.entity.PrayerTimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPrayerTimes(prayerTime: List<PrayerTimeEntity>)

    @Query("SELECT * FROM prayer_times")
    fun getAllPrayer(): Flow<List<PrayerTimeEntity>>

    @Query("SELECT * FROM prayer_times WHERE day = :day")
    suspend fun getPrayerTimeByDay(day: Int): PrayerTimeEntity?

    @Delete
    fun deletePrayerTime(prayerTime: List<PrayerTimeEntity>)

    @Query("DELETE FROM prayer_times")
    suspend fun deleteAllPrayer()

    @Update
    suspend fun updatePrayerTime(prayerTime: PrayerTimeEntity)


}