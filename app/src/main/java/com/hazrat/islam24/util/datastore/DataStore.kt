package com.hazrat.islam24.util.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hazrat.islam24.util.Constants.APP_DATA_STORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

/**
 * @author Hazrat Ummar Shaikh
 * Created on 24-12-2024
 */

class DataStore @Inject constructor(
    @Named(APP_DATA_STORE) private val appDataStore: DataStore<Preferences>,
) {

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

    }

    suspend fun savePrayerNotificationType(
        prayerName: PrayerName,
        notificationType: NotificationType
    ) {
        val key = when (prayerName) {
            PrayerName.FAJR -> FAJR_KEY
            PrayerName.DHUHR -> DHUHR_KEY
            PrayerName.ASR -> ASR_KEY
            PrayerName.MAGHRIB -> MAGHRIB_KEY
            PrayerName.ISHA -> ISHA_KEY
            else -> throw IllegalArgumentException("Invalid prayer name")
        }
        appDataStore.edit { pref ->
            pref[key] = notificationType.name
        }
    }

    fun getPrayerNotificationType(prayerName: PrayerName): Flow<NotificationType> {
        val key = when (prayerName) {
            PrayerName.FAJR -> FAJR_KEY
            PrayerName.DHUHR -> DHUHR_KEY
            PrayerName.ASR -> ASR_KEY
            PrayerName.MAGHRIB -> MAGHRIB_KEY
            PrayerName.ISHA -> ISHA_KEY
            else -> throw IllegalArgumentException("Invalid prayer name")
        }
        Log.d("DataStore", "Retrieving notification type for $prayerName")
        return appDataStore.data.map { pref ->
            val notificationTypeName = pref[key] ?: NotificationType.DEFAULT.name
            Log.d("DataStore", "Notification type for $prayerName: $notificationTypeName")
            NotificationType.valueOf(notificationTypeName)
        }
    }

    suspend fun setSelectedFajrNotification(selectedFajrNotification: Int) {
        appDataStore.edit { pref ->
            pref[SELECTED_FAJR_NOTIFICATION_KEY] = selectedFajrNotification
        }
    }

    val selectedFajrNotification: Flow<Int> = appDataStore.data.map { pref ->
        pref[SELECTED_FAJR_NOTIFICATION_KEY] ?: 0
    }

    suspend fun setSelectedDhuhrNotification(selectedDhuhrNotification: Int) {
        appDataStore.edit { pref ->
            pref[SELECTED_DHUHR_NOTIFICATION_KEY] = selectedDhuhrNotification
        }
    }

    val selectedDhuhrNotification: Flow<Int> = appDataStore.data.map { pref ->
        pref[SELECTED_DHUHR_NOTIFICATION_KEY] ?: 0
    }

    suspend fun setSelectedAsrNotification(selectedAsrNotification: Int) {
        appDataStore.edit { pref ->
            pref[SELECTED_ASR_NOTIFICATION_KEY] = selectedAsrNotification
        }
    }

    val selectedAsrNotification: Flow<Int> = appDataStore.data.map { pref ->
        pref[SELECTED_ASR_NOTIFICATION_KEY] ?: 0
    }

    suspend fun setSelectedMaghribNotification(selectedMaghribNotification: Int) {
        appDataStore.edit { pref ->
            pref[SELECTED_MAGHRIB_NOTIFICATION_KEY] = selectedMaghribNotification
        }
    }
    val selectedMaghribNotification: Flow<Int> = appDataStore.data.map { pref ->
        pref[SELECTED_MAGHRIB_NOTIFICATION_KEY] ?: 0
    }

    suspend fun setSelectedIshaNotification(selectedIshaNotification: Int) {
        appDataStore.edit { pref ->
            pref[SELECTED_ISHA_NOTIFICATION_KEY] = selectedIshaNotification
        }
    }

    val selectedIshaNotification: Flow<Int> = appDataStore.data.map { pref ->
        pref[SELECTED_ISHA_NOTIFICATION_KEY] ?: 0
    }

}

enum class PrayerName {
    FAJR,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA
}

enum class NotificationType {
    DEFAULT,
    AZAN,
    SILENT
}