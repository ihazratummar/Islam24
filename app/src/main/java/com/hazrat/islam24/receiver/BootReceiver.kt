package com.hazrat.islam24.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hazrat.islam24.core.data.database.PrayerDatabase
import com.hazrat.islam24.notification.PrayerAlarmManager
import com.hazrat.islam24.util.datastore.DataStorePreference
import com.hazrat.islam24.util.datastore.PrayerName
import com.hazrat.islam24.util.fetchPrayerTimeForNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {


    @Inject
    lateinit var prayerAlarmManager: PrayerAlarmManager

    @Inject
    lateinit var prayerTimeDatabase: PrayerDatabase

    @Inject
    lateinit var dataStorePreference: DataStorePreference

    override fun onReceive(context: Context, intent: Intent?) {



        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d("BootReceiver", "BOOT_COMPLETED action received")
                if (dataStorePreference.getFajrNotification()){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.FAJR, prayerDatabase = prayerTimeDatabase) { fajrTimeFromDatabase ->
                        prayerAlarmManager.setFajrPrayerAlarm(fajrTimeFromDatabase)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for Fajr after boot $fajrTimeFromDatabase")
                    }
                }
                if (dataStorePreference.getDhuhrNotification()){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.DHUHR, prayerDatabase = prayerTimeDatabase) { dhuhrTime ->
                        prayerAlarmManager.setDhuhrPrayerAlarm(dhuhrTime)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for Dhuhr after boot $dhuhrTime")

                    }
                }
                if (dataStorePreference.getAsrNotification()){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.ASR, prayerDatabase = prayerTimeDatabase) { asrTime ->
                        prayerAlarmManager.setAsrPrayerAlarm(asrTime)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for asr after boot $asrTime")
                    }
                }
                if (dataStorePreference.getMaghribNotification()){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.MAGHRIB, prayerDatabase = prayerTimeDatabase) { maghribTime ->
                        prayerAlarmManager.setMaghribPrayerAlarm(maghribTime)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for Maghrib after boot $maghribTime")
                    }
                }
                if (dataStorePreference.getIshaNotification()){
                    fetchPrayerTimeForNotification(prayerName = PrayerName.ISHA, prayerDatabase = prayerTimeDatabase) { ishaTime ->
                        prayerAlarmManager.setIshaPrayerAlarm(ishaTime)
                        Log.d("PrayerAlarmStart", "Scheduled alarm for Isha after boot $ishaTime")
                    }
                }
            }
        }
    }
}