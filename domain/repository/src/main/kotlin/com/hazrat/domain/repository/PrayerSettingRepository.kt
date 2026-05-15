package com.hazrat.domain.repository

import com.hazrat.database.entity.PrayerCalculationEntity
import com.hazrat.database.entity.PrayerJuristicEntity
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
     * @param method The PrayerCalculationEntity object to be inserted or updated.
     */
    suspend fun insertCalculationMethod(method: Int)

    /**
     * Inserts or updates a prayer juristic method into the database.
     * If a method with the same primary key already exists, it will be replaced.
     *
     * @param method The PrayerJuristicEntity object to be inserted or updated.
     */
    suspend fun insertJuristicMethod(method: Int)
}