package com.hazrat.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hazrat.utils.Constants.USER_DATA_SORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

/**
 * @author Hazrat Ummar Shaikh
 * Created on 24-12-2024
 */

class UserDataStore @Inject constructor(
    @param:Named(USER_DATA_SORE) private val userDataStore: DataStore<Preferences>,
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


        const val DAILY_QURAN_DATE = "DAILY_QURAN_DATE"
        const val RANDOM_AYAT_NUMBER = "RANDOM_AYAT_NUMBER"


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

        //Keys for selected prayer notification
        private val SELECTED_FAJR_NOTIFICATION_KEY = intPreferencesKey(SELECTED_FAJR_NOTIFICATION)
        private val SELECTED_DHUHR_NOTIFICATION_KEY = intPreferencesKey(SELECTED_DHUHR_NOTIFICATION)
        private val SELECTED_ASR_NOTIFICATION_KEY = intPreferencesKey(SELECTED_ASR_NOTIFICATION)
        private val SELECTED_MAGHRIB_NOTIFICATION_KEY = intPreferencesKey(SELECTED_MAGHRIB_NOTIFICATION)
        private val SELECTED_ISHA_NOTIFICATION_KEY = intPreferencesKey(SELECTED_ISHA_NOTIFICATION)
        // ---------------//

        private val DAILY_QURAN_DATE_KEY = stringPreferencesKey(DAILY_QURAN_DATE)
        private val RANDOM_AYAT_NUMBER_KEY = intPreferencesKey(RANDOM_AYAT_NUMBER)

        private val SELECTED_QIBLA_COMPASS_KEY = intPreferencesKey(SELECTED_QIBLA_COMPASS)

    }

    suspend fun clearSelectedCompassId(){
        val key = SELECTED_QIBLA_COMPASS_KEY
        userDataStore.edit { pref->
            pref.remove(key)
        }
    }

    suspend fun saveSelectedCompassId(id: Int){
        val key = SELECTED_QIBLA_COMPASS_KEY
        userDataStore.edit { pref->
            pref[key] = id
        }
    }

    val getSelectedCompassId: Flow<Int> = userDataStore.data.map { pref ->
        pref[SELECTED_QIBLA_COMPASS_KEY] ?: 1
    }

    suspend fun saveDailyQuranDate(date: String){
        val key = DAILY_QURAN_DATE_KEY
        userDataStore.edit { pref->
            pref[key] = date
        }
    }
    val getDailyQuranDate: Flow<String> = userDataStore.data.map { pref ->
        pref[DAILY_QURAN_DATE_KEY] ?: ""
    }

    suspend fun saveRandomAyatNumber(number: Int){
        val key = RANDOM_AYAT_NUMBER_KEY
        userDataStore.edit { pref->
            pref[key] = number
        }
    }
    val getRandomAyatNumber: Flow<Int> = userDataStore.data.map { pref ->
        pref[RANDOM_AYAT_NUMBER_KEY] ?: 0
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
        }
        userDataStore.edit { pref ->
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
        }
        Log.d("DataStore", "Retrieving notification type for $prayerName")
        return userDataStore.data.map { pref ->
            val notificationTypeName = pref[key] ?: NotificationType.DEFAULT.name
            Log.d("DataStore", "Notification type for $prayerName: $notificationTypeName")
            NotificationType.valueOf(notificationTypeName)
        }
    }

    suspend fun setSelectedFajrNotification(selectedFajrNotification: Int) {
        userDataStore.edit { pref ->
            pref[SELECTED_FAJR_NOTIFICATION_KEY] = selectedFajrNotification
        }
    }

    val selectedFajrNotification: Flow<Int> = userDataStore.data.map { pref ->
        pref[SELECTED_FAJR_NOTIFICATION_KEY] ?: 0
    }

    suspend fun setSelectedDhuhrNotification(selectedDhuhrNotification: Int) {
        userDataStore.edit { pref ->
            pref[SELECTED_DHUHR_NOTIFICATION_KEY] = selectedDhuhrNotification
        }
    }

    val selectedDhuhrNotification: Flow<Int> = userDataStore.data.map { pref ->
        pref[SELECTED_DHUHR_NOTIFICATION_KEY] ?: 0
    }

    suspend fun setSelectedAsrNotification(selectedAsrNotification: Int) {
        userDataStore.edit { pref ->
            pref[SELECTED_ASR_NOTIFICATION_KEY] = selectedAsrNotification
        }
    }

    val selectedAsrNotification: Flow<Int> = userDataStore.data.map { pref ->
        pref[SELECTED_ASR_NOTIFICATION_KEY] ?: 0
    }

    suspend fun setSelectedMaghribNotification(selectedMaghribNotification: Int) {
        userDataStore.edit { pref ->
            pref[SELECTED_MAGHRIB_NOTIFICATION_KEY] = selectedMaghribNotification
        }
    }
    val selectedMaghribNotification: Flow<Int> = userDataStore.data.map { pref ->
        pref[SELECTED_MAGHRIB_NOTIFICATION_KEY] ?: 0
    }

    suspend fun setSelectedIshaNotification(selectedIshaNotification: Int) {
        userDataStore.edit { pref ->
            pref[SELECTED_ISHA_NOTIFICATION_KEY] = selectedIshaNotification
        }
    }

    val selectedIshaNotification: Flow<Int> = userDataStore.data.map { pref ->
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