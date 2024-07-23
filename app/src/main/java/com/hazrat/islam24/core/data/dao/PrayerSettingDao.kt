package com.hazrat.islam24.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.hazrat.islam24.core.data.entity.PrayerCalculationEntity
import com.hazrat.islam24.core.data.entity.PrayerJuristicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerSettingDao {

    /**
     * Retrieves the prayer method settings from the database.
     *
     * @return Flow representing the list of prayer method settings as PrayerSettingEntity objects.
     */
    @Query("SELECT * FROM calculation_method_entity")
    fun getCalculationMethod(): Flow<PrayerCalculationEntity>

    @Query("SELECT * FROM juristic_method_entity")
    fun getJuristicMethod(): Flow<PrayerJuristicEntity>

    /**
     * Inserts or updates a prayer method setting into the database.
     * If a setting with the same primary key already exists, it will be replaced.
     *
     * @param prayerCalculationEntity The PrayerSettingEntity object to be inserted or updated.
     */
    @Upsert()
    suspend fun insertCalculationMethod(prayerCalculationEntity: PrayerCalculationEntity)

    @Upsert()
    suspend fun insertJuristicMethod(prayerJuristicEntity: PrayerJuristicEntity)

    /**
     * Deletes all prayer method settings from the database.
     * This method is useful for cleaning up the database.
     */
    @Query("DELETE FROM calculation_method_entity")
    suspend fun deleteAllMethod()

    @Query("DELETE FROM juristic_method_entity")
    suspend fun deleteAllJuristic()

    /**
     * Deletes a specific prayer method setting from the database.
     *
     * @param prayerSettingEntity The PrayerSettingEntity object to be deleted.
     */
    @Delete
    suspend fun deleteMethod(prayerSettingEntity: PrayerCalculationEntity)

    @Delete
    suspend fun deleteJuristic(prayerJuristicEntity: PrayerJuristicEntity)
}