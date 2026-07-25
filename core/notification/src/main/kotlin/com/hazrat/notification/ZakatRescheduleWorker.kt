package com.hazrat.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hazrat.database.dao.ZakatDao
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Background WorkManager worker that reschedules all active Zakat Hawl alarms on device reboot,
 * time changes, or package updates.
 *
 * @author Hazrat Ummar Shaikh
 */
class ZakatRescheduleWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val zakatDao: ZakatDao by inject()
    private val zakatAlarmScheduler: ZakatAlarmScheduler by inject()

    override suspend fun doWork(): Result {
        Log.d("ZakatRescheduleWorker", "Starting Zakat alarm reschedule background job...")

        try {
            val zakatList = zakatDao.getZakatList().firstOrNull() ?: emptyList()
            val now = System.currentTimeMillis()

            zakatList.forEach { zakat ->
                // Calculate Hawl due date: 1 lunar year (~354 days) after calculation date
                val defaultHawlMillis = zakat.date + (354L * 24 * 60 * 60 * 1000)

                val triggerTime = if (defaultHawlMillis > now) {
                    defaultHawlMillis
                } else {
                    // If 1 lunar year has passed, schedule for next year
                    now + (354L * 24 * 60 * 60 * 1000)
                }

                zakatAlarmScheduler.scheduleZakatReminder(
                    zakatId = zakat.id,
                    triggerTimeMillis = triggerTime,
                    zakatAmount = zakat.zakatAmount
                )
            }

            Log.d("ZakatRescheduleWorker", "Successfully rescheduled Zakat alarms for ${zakatList.size} records.")
            return Result.success()
        } catch (e: Exception) {
            Log.e("ZakatRescheduleWorker", "Error rescheduling Zakat alarms", e)
            return Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "ZakatRescheduleWorker"

        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<ZakatRescheduleWorker>()
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                request
            )
            Log.d("ZakatRescheduleWorker", "Enqueued ZakatRescheduleWorker.")
        }
    }
}
