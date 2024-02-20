package com.hazrat.islam24.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.data.entity.PrayerSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerSettingDao {

    //Method

    @Query("SELECT * FROM method_entity")
    fun getMethod(): Flow<List<PrayerSettingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethod(prayerSettingEntity: PrayerSettingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMethod(prayerSettingEntity: PrayerSettingEntity)

    @Query("DELETE FROM method_entity")
    suspend fun deleteAllMethod()

    @Delete
    suspend fun deleteMethod(prayerSettingEntity: PrayerSettingEntity)


}