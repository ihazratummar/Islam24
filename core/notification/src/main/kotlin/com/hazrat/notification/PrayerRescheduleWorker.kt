package com.hazrat.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hazrat.database.database.PrayerDatabase
import com.hazrat.database.mapper.toMinimalPrayerData
import com.hazrat.datastore.UserDataStore
import com.hazrat.model.Prayer
import com.hazrat.utils.DateUtil
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Enterprise-grade worker responsible for calculating and scheduling the next batch
 * of prayer alarms. Ensures reliability by acting as the single source of rescheduling logic.
 */
class PrayerRescheduleWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val prayerAlarmManager: PrayerAlarmScheduler by inject()
    private val prayerTimeDatabase: PrayerDatabase by inject()
    private val userDataStore: UserDataStore by inject()

    override suspend fun doWork(): Result {
        Log.d("PrayerRescheduleWorker", "Rescheduling alarms...")

        return try {
            val todayDate = DateUtil.getCurrentDate()
            val tomorrowDate = DateUtil.getTomorrowDate()

            val todayData = prayerTimeDatabase.prayerTimeDao()
                .getPrayerTimeForToday(todayDate)
                .firstOrNull()?.toMinimalPrayerData()

            val tomorrowData = prayerTimeDatabase.prayerTimeDao()
                .getPrayerTimeForToday(tomorrowDate)
                .firstOrNull()?.toMinimalPrayerData()

            if (todayData != null && tomorrowData != null) {
                val enabledPrayers = Prayer.entries.filter { prayer ->
                    userDataStore.isPrayerNotificationEnabled(prayer)
                }.toSet()

                prayerAlarmManager.rescheduleAll(
                    today = todayData,
                    tomorrow = tomorrowData,
                    enabledPrayers = enabledPrayers
                )
                Result.success()
            } else {
                Log.i("PrayerRescheduleWorker", "No prayer data available yet. Skipping rescheduling.")
                // Return success so we don't spam retries; 
                // the Janitor or Settings change will trigger this again when data arrives.
                Result.success()
            }
        } catch (e: Exception) {
            Log.e("PrayerRescheduleWorker", "Error during alarm rescheduling", e)
            Result.retry()
        }
    }

    companion object {
        fun enqueue(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<PrayerRescheduleWorker>().build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
