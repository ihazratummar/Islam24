package com.hazrat.prayertime.data.repository


import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerSettingRepository
import com.hazrat.model.Prayer
import com.hazrat.model.PrayerNotificationSettings
import com.hazrat.ui.common.PrayerType
import kotlinx.coroutines.flow.Flow

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

    override suspend fun prayerNotificationEnabled(
        prayer: Prayer,
        enabled: Boolean
    ) {
        userDataStore.setPrayerNotificationEnabled(prayerName = prayer, enabled = enabled)
    }

    override fun getNotificationEnable(): Flow<PrayerNotificationSettings> {
        return userDataStore.notificationSettingsFlow
    }
}

fun PrayerType.toPrayerName() : Prayer {
    return when(this){
        PrayerType.FAJR -> Prayer.FAJR
        PrayerType.DHUHR ->  Prayer.DHUHR
        PrayerType.ASR ->  Prayer.ASR
        PrayerType.MAGHRIB ->  Prayer.MAGHRIB
        PrayerType.ISHA -> Prayer.ISHA
        else -> Prayer.FAJR
    }
}