package com.hazrat.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hazrat.model.Prayer
import com.hazrat.model.PrayerNotificationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * @author Hazrat Ummar Shaikh
 * Created on 24-12-2024
 */

class UserDataStore(
    private val userDataStore: DataStore<Preferences>,
) {

    /*
    -----------------
    DYNAMIC KEYS
    ----------------
     */

    private fun notificationEnabledKey(prayerName: Prayer) =
        booleanPreferencesKey("${prayerName.key}_notification_enabled")

    private fun notificationTypeKey(prayerName: Prayer) =
        stringPreferencesKey("${prayerName.key}_notification_type")


    companion object {

        //Constants
        const val FAJR = "Fajr"
        const val DHUHR = "Dhuhr"
        const val ASR = "Asr"
        const val MAGHRIB = "Maghrib"
        const val ISHA = "Isha"

        const val SELECTED_FAJR_NOTIFICATION = "SELECTED_FAJR_NOTIFICATION"
        const val SELECTED_DHUHR_NOTIFICATION = "SELECTED_DHUHR_NOTIFICATION"
        const val SELECTED_ASR_NOTIFICATION = "SELECTED_ASR_NOTIFICATION"
        const val SELECTED_MAGHRIB_NOTIFICATION = "SELECTED_MAGHRIB_NOTIFICATION"
        const val SELECTED_ISHA_NOTIFICATION = "SELECTED_ISHA_NOTIFICATION"


        const val SELECTED_QIBLA_COMPASS = "SELECTED_QIBLA_COMPASS"

        const val PRAYER_CALCULATION_METHOD = "PRAYER_CALCULATION_METHOD"
        const val PRAYER_JURISTIC_METHOD = "PRAYER_JURISTIC_METHOD"

        /*
        ******************--------------------------*************************
         */
        // Keys for each prayer time
        private val FAJR_KEY = stringPreferencesKey(FAJR)
        private val DHUHR_KEY = stringPreferencesKey(DHUHR)
        private val ASR_KEY = stringPreferencesKey(ASR)
        private val MAGHRIB_KEY = stringPreferencesKey(MAGHRIB)
        private val ISHA_KEY = stringPreferencesKey(ISHA)

        //Keys for selected prayer notification
        private val SELECTED_FAJR_NOTIFICATION_KEY = intPreferencesKey(SELECTED_FAJR_NOTIFICATION)
        private val SELECTED_DHUHR_NOTIFICATION_KEY = intPreferencesKey(SELECTED_DHUHR_NOTIFICATION)
        private val SELECTED_ASR_NOTIFICATION_KEY = intPreferencesKey(SELECTED_ASR_NOTIFICATION)
        private val SELECTED_MAGHRIB_NOTIFICATION_KEY =
            intPreferencesKey(SELECTED_MAGHRIB_NOTIFICATION)
        private val SELECTED_ISHA_NOTIFICATION_KEY = intPreferencesKey(SELECTED_ISHA_NOTIFICATION)

        // ---------------//
        private val SELECTED_QIBLA_COMPASS_KEY = intPreferencesKey(SELECTED_QIBLA_COMPASS)

        private val PrayerCalculationMethodKey = intPreferencesKey(PRAYER_CALCULATION_METHOD)
        private val PrayerJuristicMethodKey = intPreferencesKey(PRAYER_JURISTIC_METHOD)

    }


    /*
    -------------------
    NOTIFICATION ENABLE
    -------------------
     */

    suspend fun setPrayerNotificationEnabled(prayerName: Prayer, enabled: Boolean) {
        val key = notificationEnabledKey(prayerName = prayerName)
        userDataStore.edit { pref ->
            pref[key] = enabled
            Log.d("DataStore", "${prayerName.key} $enabled")
        }
    }

    suspend fun isPrayerNotificationEnabled(
        prayerName: Prayer
    ): Boolean {

        val key = notificationEnabledKey(prayerName)

        return userDataStore.data.first()[key] ?: true
    }



    val notificationSettingsFlow: Flow<PrayerNotificationSettings> =
        userDataStore.data.map { pref ->
            PrayerNotificationSettings(
                fajr = pref[notificationEnabledKey(prayerName = Prayer.FAJR)] ?: true,
                dhuhr = pref[notificationEnabledKey(prayerName = Prayer.DHUHR)] ?: true,
                asr = pref[notificationEnabledKey(prayerName = Prayer.ASR)] ?: true,
                maghrib = pref[notificationEnabledKey(prayerName = Prayer.MAGHRIB)] ?: true,
                isha = pref[notificationEnabledKey(prayerName = Prayer.ISHA)] ?: true,
            )
        }


    suspend fun clearSelectedCompassId() {
        val key = SELECTED_QIBLA_COMPASS_KEY
        userDataStore.edit { pref ->
            pref.remove(key)
        }
    }

    suspend fun saveSelectedCompassId(id: Int) {
        val key = SELECTED_QIBLA_COMPASS_KEY
        userDataStore.edit { pref ->
            pref[key] = id
        }
    }

    val getSelectedCompassId: Flow<Int> = userDataStore.data.map { pref ->
        pref[SELECTED_QIBLA_COMPASS_KEY] ?: 1
    }


    suspend fun savePrayerNotificationType(
        prayerName: Prayer,
        notificationType: NotificationType
    ) {
        val key = when (prayerName) {
            Prayer.FAJR -> FAJR_KEY
            Prayer.DHUHR -> DHUHR_KEY
            Prayer.ASR -> ASR_KEY
            Prayer.MAGHRIB -> MAGHRIB_KEY
            Prayer.ISHA ->ISHA_KEY
        }
        userDataStore.edit { pref ->
            pref[key] = notificationType.name
        }
    }

    fun getPrayerNotificationType(prayerName: Prayer): Flow<NotificationType> {
        val key = when (prayerName) {
            Prayer.FAJR -> FAJR_KEY
            Prayer.DHUHR -> DHUHR_KEY
            Prayer.ASR -> ASR_KEY
            Prayer.MAGHRIB -> MAGHRIB_KEY
            Prayer.ISHA ->ISHA_KEY
        }
        return userDataStore.data.map { pref ->
            val notificationTypeName = pref[key] ?: NotificationType.DEFAULT.name
            NotificationType.valueOf(notificationTypeName)
        }
    }



    suspend fun saveSetPrayerCalculationMethod(calculationMethod: Int): Boolean {
        return try {
            userDataStore.edit { preferences ->
                preferences[PrayerCalculationMethodKey] = calculationMethod
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    val getPrayerCalculationMethod: Flow<Int> =
        userDataStore.data.map { preferences ->
            preferences[PrayerCalculationMethodKey] ?: 0
        }

    suspend fun getPrayerCalculationMethod(): Int {
        return userDataStore.data.first()[PrayerCalculationMethodKey] ?: 0
    }


    suspend fun savePrayerJuristicMethod(method: Int): Boolean {
        return try {
            userDataStore.edit { preferences ->
                preferences[PrayerJuristicMethodKey] = method
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun getPrayerJuristicMethod(): Int {
        return userDataStore.data.first()[PrayerJuristicMethodKey] ?: 0
    }

    val getPrayerJuristicMethod: Flow<Int> =
        userDataStore.data.map { preferences ->
            preferences[PrayerJuristicMethodKey] ?: 0
        }

}


enum class NotificationType {
    DEFAULT,
    AZAN,
    SILENT
}

