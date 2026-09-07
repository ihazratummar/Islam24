package com.hazrat.islam24.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hazrat.datastore.TokenStorage
import com.hazrat.domain.repository.SyncRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber


/**
 * @author hazratummar
 * Created on 14/08/26
 */

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
): CoroutineWorker(appContext = appContext, workerParams), KoinComponent {

    private val syncRepository: SyncRepository by inject()
    private val tokenStorage : TokenStorage by inject()

    override suspend fun doWork(): Result {
        // Guard check: Avoid running sync and wasting battery if user is logged out
        val token = tokenStorage.getAccessToken()
        if (token.isNullOrBlank()) {
            Timber.tag("SyncWorker").d( "User not logged in. Skipping background sync.")
            return Result.success()
        }

        Timber.tag("SyncWorker").d("Starting background sync work for logged-in user...")
        return try {
            val success = syncRepository.performFullSync()
            if (success) {
                Timber.tag("SyncWorker").d("Background sync completed successfully.")
                Result.success()
            } else {
                Timber.tag("SyncWorker").w("Sync returned false, retrying...")
                Result.retry()
            }
        } catch (e: java.io.IOException) {
            // Network issue: retry when internet is restored
            Timber.tag("SyncWorker").e("Sync failed with network error: ${e.message}. Retrying...")
            Result.retry()
        } catch (e: Exception) {
            Timber.tag("SyncWorker").e("Sync failed with exception: ${e.message}")
            Result.retry()
        }
    }
}