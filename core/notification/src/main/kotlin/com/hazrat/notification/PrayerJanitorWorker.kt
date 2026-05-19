package com.hazrat.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hazrat.database.database.PrayerDatabase
import com.hazrat.datastore.UserDataStore
import com.hazrat.model.Prayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Worker that periodically checks and ensures all prayer alarms are scheduled.
 * Acts as a fail-safe mechanism.
 */
class PrayerJanitorWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val prayerAlarmManager: PrayerAlarmScheduler by inject()
    private val prayerTimeDatabase: PrayerDatabase by inject()
    private val userDataStore: UserDataStore by inject()

    override suspend fun doWork(): Result {
        Log.d("PrayerJanitorWorker", "Starting periodic alarm verification...")
        
        return try {
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
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("PrayerJanitorWorker", "Error during alarm verification", e)
            Result.retry()
        }
    }
}
