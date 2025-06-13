package com.hazrat.islam24.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.core.data.entity.PrayerCalculationEntity
import com.hazrat.islam24.core.data.entity.PrayerJuristicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerSettingDao {

    /**
     * Retrieves all prayer calculation method settings from the database.
     *
     * @return Flow representing the list of prayer calculation method settings as PrayerCalculationEntity objects.
     */
    @Query("SELECT * FROM calculation_method_entity WHERE  id = 1")
    fun getCalculationMethod(): Flow<PrayerCalculationEntity?>

    /**
     * Retrieves all prayer juristic method settings from the database.
     *
     * @return Flow representing the list of prayer juristic method settings as PrayerJuristicEntity objects.
     */
    @Query("SELECT * FROM juristic_method_entity WHERE  id = 1")
    fun getJuristicMethod(): Flow<PrayerJuristicEntity?>

    /**
     * Inserts or updates a prayer calculation method setting into the database.
     * If a setting with the same primary key already exists, it will be replaced.
     *
     * @param prayerCalculationEntity The PrayerCalculationEntity object to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculationMethod(prayerCalculationEntity: PrayerCalculationEntity)

    /**
     * Inserts or updates a prayer juristic method setting into the database.
     * If a setting with the same primary key already exists, it will be replaced.
     *
     * @param prayerJuristicEntity The PrayerJuristicEntity object to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJuristicMethod(prayerJuristicEntity: PrayerJuristicEntity)
}
