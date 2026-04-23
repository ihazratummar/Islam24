package com.hazrat.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hazrat.database.database.PrayerDatabase
import com.hazrat.datastore.DataStorePreference
import com.hazrat.datastore.PrayerName
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author Hazrat Ummar Shaikh
 */


class BootReceiver : BroadcastReceiver(), KoinComponent {


    private val prayerAlarmManager: PrayerAlarmManager by inject()
    private val prayerTimeDatabase: PrayerDatabase by inject()
    private val dataStorePreference: DataStorePreference by inject()

    override fun onReceive(context: Context, intent: Intent?) {



        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d("BootReceiver", "BOOT_COMPLETED action received")
                if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_FAJR_NOTIFICATION)){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.FAJR, prayerDatabase = prayerTimeDatabase) { fajrTimeFromDatabase ->
                        prayerAlarmManager.setFajrPrayerAlarm(fajrTimeFromDatabase)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for Fajr after boot $fajrTimeFromDatabase")
                    }
                }
                if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_DHUHR_NOTIFICATION)){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.DHUHR, prayerDatabase = prayerTimeDatabase) { dhuhrTime ->
                        prayerAlarmManager.setDhuhrPrayerAlarm(dhuhrTime)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for Dhuhr after boot $dhuhrTime")

                    }
                }
                if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_ASR_NOTIFICATION)){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.ASR, prayerDatabase = prayerTimeDatabase) { asrTime ->
                        prayerAlarmManager.setAsrPrayerAlarm(asrTime)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for asr after boot $asrTime")
                    }
                }
                if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_MAGHRIB_NOTIFICATION)){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.MAGHRIB, prayerDatabase = prayerTimeDatabase) { maghribTime ->
                        prayerAlarmManager.setMaghribPrayerAlarm(maghribTime)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for Maghrib after boot $maghribTime")
                    }
                }
                if (dataStorePreference.getPrayerNotification(DataStorePreference.KEY_ISHA_NOTIFICATION)){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.ISHA, prayerDatabase = prayerTimeDatabase) { ishaTime ->
                        prayerAlarmManager.setIshaPrayerAlarm(ishaTime)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for Isha after boot $ishaTime")
                    }
                }
            }
        }
    }
}