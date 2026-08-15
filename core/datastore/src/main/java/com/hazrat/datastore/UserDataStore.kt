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

    private fun preAlertOffsetKey(prayerName: Prayer) =
        intPreferencesKey("${prayerName.key}_pre_alert_offset")

    private fun vibrationEnabledKey(prayerName: Prayer) =
        booleanPreferencesKey("${prayerName.key}_vibration_enabled")



    companion object {

        //Constants
        const val FAJR = "Fajr"
        const val DHUHR = "Dhuhr"
        const val ASR = "Asr"
        const val MAGHRIB = "Maghrib"
        const val ISHA = "Isha"


        const val SELECTED_QIBLA_COMPASS = "SELECTED_QIBLA_COMPASS"

        /*
        ******************--------------------------*************************
         */
        // Keys for each prayer time
        private val FAJR_KEY = stringPreferencesKey(FAJR)
        private val DHUHR_KEY = stringPreferencesKey(DHUHR)
        private val ASR_KEY = stringPreferencesKey(ASR)
        private val MAGHRIB_KEY = stringPreferencesKey(MAGHRIB)
        private val ISHA_KEY = stringPreferencesKey(ISHA)

        // ---------------//
        private val SELECTED_QIBLA_COMPASS_KEY = intPreferencesKey(SELECTED_QIBLA_COMPASS)
        private val MasterNotificationEnabledKey = booleanPreferencesKey("MASTER_NOTIFICATION_ENABLED")
        private val TotalSupportedAmountUSDKey = androidx.datastore.preferences.core.doublePreferencesKey("TOTAL_SUPPORTED_AMOUNT_USD")
        private val IsSubscribedKey = booleanPreferencesKey("IS_SUBSCRIBED")
        private val LastKnownLatitudeKey = androidx.datastore.preferences.core.doublePreferencesKey("LAST_KNOWN_LATITUDE")
        private val LastKnownLongitudeKey = androidx.datastore.preferences.core.doublePreferencesKey("LAST_KNOWN_LONGITUDE")

    }

    val isSubscribed: Flow<Boolean> = userDataStore.data.map { pref ->
        pref[IsSubscribedKey] ?: false
    }

    suspend fun setIsSubscribed(isSubscribed: Boolean) {
        userDataStore.edit { pref ->
            pref[IsSubscribedKey] = isSubscribed
        }
    }

    suspend fun setMasterNotificationEnabled(enabled: Boolean) {
        userDataStore.edit { pref ->
            pref[MasterNotificationEnabledKey] = enabled
        }
    }

    val isMasterNotificationEnabled: Flow<Boolean> = userDataStore.data.map { pref ->
        pref[MasterNotificationEnabledKey] ?: true
    }

    val totalSupportedAmountUSD: Flow<Double> = userDataStore.data.map { pref ->
        pref[TotalSupportedAmountUSDKey] ?: 0.0
    }

    suspend fun addSupportedAmountUSD(amount: Double) {
        userDataStore.edit { pref ->
            val current = pref[TotalSupportedAmountUSDKey] ?: 0.0
            pref[TotalSupportedAmountUSDKey] = current + amount
        }
    }

    suspend fun setTotalSupportedAmountUSD(total: Double) {
        userDataStore.edit { pref ->
            pref[TotalSupportedAmountUSDKey] = total
        }
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


    /*
    -------------------------------------------
    PRE-ALERT OFFSET, VIBRATION & AZAN SOUND
    -------------------------------------------
     */


    suspend fun setPrayerVibrationEnabled(prayerName: Prayer, enabled: Boolean) {
        val key = vibrationEnabledKey(prayerName)
        userDataStore.edit { pref ->
            pref[key] = enabled
        }
    }


    /**
     * Location states
     */
    suspend fun saveLastKnownLocation(latitude: Double, longitude: Double) {
        userDataStore.edit { pref ->
            pref[LastKnownLatitudeKey] = latitude
            pref[LastKnownLongitudeKey] = longitude
        }
    }

    suspend fun getLastKnownLocationSync(): Pair<Double, Double>? {
        val pref = userDataStore.data.first()
        val lat = pref[LastKnownLatitudeKey]
        val lng = pref[LastKnownLongitudeKey]
        return if (lat != null && lng != null && lat != 0.0 && lng != 0.0) {
            Pair(lat, lng)
        } else null
    }
}


enum class NotificationType {
    DEFAULT,
    AZAN,
    SILENT
}

