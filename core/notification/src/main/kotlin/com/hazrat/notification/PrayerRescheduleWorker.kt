package com.hazrat.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hazrat.database.converter.NotificationSettingsMapEntity
import com.hazrat.database.dao.prayer.PrayerSettingDao
import com.hazrat.database.database.PrayerDatabase
import com.hazrat.database.mapper.toMinimalPrayerData
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
    private val prayerSettingDao: PrayerSettingDao by inject()

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
                val settingsMap = prayerSettingDao.getSettings()?.notificationSettingsJson?: NotificationSettingsMapEntity()
                val enabledPrayers = Prayer.entries.filter { prayer ->
                    when(prayer){
                        Prayer.FAJR -> settingsMap.fajr.enabled
                        Prayer.DHUHR -> settingsMap.dhuhr.enabled
                        Prayer.ASR -> settingsMap.asr.enabled
                        Prayer.MAGHRIB -> settingsMap.maghrib.enabled
                        Prayer.ISHA -> settingsMap.isha.enabled
                    }
                }.toSet()

                val preAlertOffsets = Prayer.entries.associateWith { prayer ->
                    when(prayer){
                        Prayer.FAJR -> settingsMap.fajr.offsetMinutes
                        Prayer.DHUHR -> settingsMap.dhuhr.offsetMinutes
                        Prayer.ASR -> settingsMap.asr.offsetMinutes
                        Prayer.MAGHRIB -> settingsMap.maghrib.offsetMinutes
                        Prayer.ISHA -> settingsMap.isha.offsetMinutes
                    }
                }

                prayerAlarmManager.rescheduleAll(
                    today = todayData,
                    tomorrow = tomorrowData,
                    enabledPrayers = enabledPrayers,
                    preAlertOffsets = preAlertOffsets
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
            WorkManager.getInstance(context).enqueueUniqueWork(
                "PrayerRescheduleWork",
                androidx.work.ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}
