package com.hazrat.notification

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
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
 * Receiver responsible for rescheduling all prayer alarms when system state changes
 * (Timezone change, Time set, App update, etc.)
 */
class RescheduleReceiver : BroadcastReceiver(), KoinComponent {

    private val prayerAlarmManager: PrayerAlarmScheduler by inject()
    private val prayerTimeDatabase: PrayerDatabase by inject()
    private val userDataStore: UserDataStore by inject()

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        Log.d("RescheduleReceiver", "Received action: $action")

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                rescheduleAllAlarms()
            } catch (e: Exception) {
                Log.e("RescheduleReceiver", "Error rescheduling alarms", e)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun rescheduleAllAlarms() {
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
                    Log.d("RescheduleReceiver", "Rescheduled alarm for ${prayer.name} at $prayerTime")
                }
            }
        }
    }
}
