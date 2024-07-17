package com.hazrat.islam24.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.core.data.entity.PrayerSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerSettingDao {

    /**
     * Retrieves the prayer method settings from the database.
     *
     * @return Flow representing the list of prayer method settings as PrayerSettingEntity objects.
     */
    @Query("SELECT * FROM method_entity")
    fun getMethod(): Flow<List<PrayerSettingEntity>>

    /**
     * Inserts or updates a prayer method setting into the database.
     * If a setting with the same primary key already exists, it will be replaced.
     *
     * @param prayerSettingEntity The PrayerSettingEntity object to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethod(prayerSettingEntity: PrayerSettingEntity)

    /**
     * Updates a prayer method setting in the database.
     * This method assumes that the setting already exists.
     *
     * @param prayerSettingEntity The updated PrayerSettingEntity object.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMethod(prayerSettingEntity: PrayerSettingEntity)

    /**
     * Deletes all prayer method settings from the database.
     * This method is useful for cleaning up the database.
     */
    @Query("DELETE FROM method_entity")
    suspend fun deleteAllMethod()

    /**
     * Deletes a specific prayer method setting from the database.
     *
     * @param prayerSettingEntity The PrayerSettingEntity object to be deleted.
     */
    @Delete
    suspend fun deleteMethod(prayerSettingEntity: PrayerSettingEntity)
}