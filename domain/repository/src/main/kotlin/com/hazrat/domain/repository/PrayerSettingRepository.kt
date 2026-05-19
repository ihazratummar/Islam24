package com.hazrat.domain.repository

interface PrayerSettingRepository {

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