package com.hazrat.islam24.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hazrat.islam24.core.data.database.PrayerDatabase
import com.hazrat.islam24.util.DateUtil.getCurrentDate
import com.hazrat.islam24.util.datastore.DataStorePreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.concurrent.thread

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
                    fetchFajrPrayerTime { fajrTimeFromDatabase ->
                        prayerAlarmManager.setFajrPrayerAlarm(fajrTimeFromDatabase)
                        Log.d("BootReceiver", "Scheduled alarm for Fajr $fajrTimeFromDatabase")
                    }
                }
                if (dataStorePreference.getDhuhrNotification()){
                    fetchDhuhrPrayerTime { dhuhrTime ->
                        prayerAlarmManager.setDhuhrPrayerAlarm(dhuhrTime)
                        Log.d("BootReceiver", "Scheduled alarm for Dhuhr $dhuhrTime")

                    }
                }
                if (dataStorePreference.getAsrNotification()){
                    fetchAsrPrayerTime { asrTime ->
                        prayerAlarmManager.setAsrPrayerAlarm(asrTime)
                        Log.d("BootReceiver", "Scheduled alarm for asr $asrTime")
                    }
                }
                if (dataStorePreference.getMaghribNotification()){
                    fetchMaghribPrayerTime { maghribTime ->
                        prayerAlarmManager.setMaghribPrayerAlarm(maghribTime)
                        Log.d("BootReceiver", "Scheduled alarm for Maghrib $maghribTime")
                    }
                }
                if (dataStorePreference.getIshaNotification()){
                    fetchIshaPrayerTime { ishaTime ->
                        prayerAlarmManager.setIshaPrayerAlarm(ishaTime)
                        Log.d("BootReceiver", "Scheduled alarm for Isha $ishaTime")
                    }
                }
            }
        }
    }


    private fun fetchFajrPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime =
                prayerTimeDatabase.prayerTimeDao().getFajrTimeForTheDay(getCurrentDate())
            callback(prayerTime)
        }
    }

    private fun fetchDhuhrPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime =
                prayerTimeDatabase.prayerTimeDao().getDhuhrTimeForTheDay(getCurrentDate())
            callback(prayerTime)
        }
    }

    private fun fetchAsrPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime = prayerTimeDatabase.prayerTimeDao().getAsrTimeForTheDay(getCurrentDate())
            callback(prayerTime)
        }
    }

    private fun fetchMaghribPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime = prayerTimeDatabase.prayerTimeDao().getMaghribTimeForTheDay(
                getCurrentDate()
            )
            callback(prayerTime)
        }
    }

    private fun fetchIshaPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime =
                prayerTimeDatabase.prayerTimeDao().getIshaTimeForTheDay(getCurrentDate())
            callback(prayerTime)
        }
    }
}