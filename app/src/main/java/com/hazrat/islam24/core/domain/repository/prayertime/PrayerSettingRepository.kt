package com.hazrat.islam24.core.domain.repository.prayertime

import com.hazrat.islam24.core.data.entity.PrayerCalculationEntity
import com.hazrat.islam24.core.data.entity.PrayerJuristicEntity
import kotlinx.coroutines.flow.Flow

interface PrayerSettingRepository {

    /**
     * Retrieves the prayer calculation method from the database.
     *
     * @return Flow representing the prayer calculation method as a PrayerCalculationEntity object.
     */
    suspend fun getCalculationMethod(): Flow<PrayerCalculationEntity?>

    /**
     * Retrieves the prayer juristic method from the database.
     *
     * @return Flow representing the prayer juristic method as a PrayerJuristicEntity object.
     */
    suspend fun getJuristicMethod(): Flow<PrayerJuristicEntity?>

    /**
     * Inserts or updates a prayer calculation method into the database.
     * If a method with the same primary key already exists, it will be replaced.
     *
     * @param prayerSettingEntity The PrayerCalculationEntity object to be inserted or updated.
     */
    suspend fun insertCalculationMethod(prayerSettingEntity: PrayerCalculationEntity)

    /**
     * Inserts or updates a prayer juristic method into the database.
     * If a method with the same primary key already exists, it will be replaced.
     *
     * @param prayerSettingEntity The PrayerJuristicEntity object to be inserted or updated.
     */
    suspend fun insertJuristicMethod(prayerSettingEntity: PrayerJuristicEntity)
}