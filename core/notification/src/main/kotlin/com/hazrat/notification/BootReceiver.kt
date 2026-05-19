package com.hazrat.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hazrat.database.database.PrayerDatabase
import com.hazrat.datastore.UserDataStore
import com.hazrat.model.Prayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author Hazrat Ummar Shaikh
 */


class BootReceiver : BroadcastReceiver(), KoinComponent {


    private val prayerAlarmManager: PrayerAlarmScheduler by inject()
    private val prayerTimeDatabase: PrayerDatabase by inject()

    private val userDataStore: UserDataStore by inject()

    override fun onReceive(context: Context, intent: Intent?) {
        val pendinResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (intent?.action) {
                    Intent.ACTION_BOOT_COMPLETED -> {
                        Log.d("BootReceiver", "BOOT_COMPLETED action received")
                        Prayer.entries.forEach { prayer ->
                            if (userDataStore.isPrayerNotificationEnabled(prayerName = prayer)) {
                                fetchPrayerTimeForNotification(
                                    prayerName = prayer,
                                    prayerDatabase = prayerTimeDatabase
                                ) { prayerTime ->
                                    prayerAlarmManager.setPrayerAlarm(
                                        prayerName = prayer,
                                        prayerTime = prayerTime
                                    )
                                    Log.d("BootReceiver", "${prayer.name} Set")
                                }
                            }

                        }
                    }
                }
            } finally {
                pendinResult.finish()
            }
        }
    }
}