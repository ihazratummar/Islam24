package com.hazrat.prayertime.data.repository

import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerSettingRepository

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-05-2025
 */

class PrayerSettingRepositoryImpl(
    private val userDataStore: UserDataStore
) : PrayerSettingRepository {

    override suspend fun insertCalculationMethod(method: Int) {
        userDataStore.saveSetPrayerCalculationMethod(calculationMethod = method)
    }

    override suspend fun insertJuristicMethod(method: Int) {
        userDataStore.savePrayerJuristicMethod(method= method)
    }
}