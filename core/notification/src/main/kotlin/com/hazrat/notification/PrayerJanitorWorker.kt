package com.hazrat.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
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
            // Enterprise-grade: Reuse the same reschedule logic for the janitor
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
                Log.w("PrayerJanitorWorker", "Missing prayer data for janitor verification")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("PrayerJanitorWorker", "Error during alarm verification", e)
            Result.retry()
        }
    }
}
