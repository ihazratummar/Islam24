package com.hazrat.islam24.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.hazrat.domain.repository.AppSyncScheduler
import java.util.concurrent.TimeUnit


/**
 * @author hazratummar
 * Created on 14/08/26
 */

class SyncScheduler(
    private val context: Context
): AppSyncScheduler {

    companion object {
        private const val PERIODIC_SYNC_WORK_NAME = "islam24_periodic_sync"
        private const val ONE_TIME_SYNC_WORK_NAME = "islam24_immediate_sync"
    }


    private val syncConstraints  = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    // One Time
    override fun scheduleImmediateSync() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(syncConstraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            ONE_TIME_SYNC_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }


    override fun schedulePeriodicSync() {
        val periodicRequest = PeriodicWorkRequestBuilder<SyncWorker>(6, TimeUnit.HOURS)
            .setConstraints(syncConstraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30 , TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PERIODIC_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }

}