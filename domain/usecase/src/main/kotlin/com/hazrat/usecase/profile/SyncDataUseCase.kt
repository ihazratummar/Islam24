package com.hazrat.usecase.profile

import com.hazrat.domain.repository.AppSyncScheduler
import com.hazrat.domain.repository.SyncRepository

/**
 * UseCase to execute cloud data synchronization and recovery.
 * 1. Performs immediate full sync (restoring remote bookmarks, khatam plans, recent reads, prayer logs & settings).
 * 2. Schedules ongoing background periodic sync.
 *
 * @author hazratummar
 * Created on 14/08/26
 */
class SyncDataUseCase(
    private val syncRepository: SyncRepository,
    private val appSyncScheduler: AppSyncScheduler
) {

    suspend operator fun invoke(): Boolean {
        val success = try {
            syncRepository.performFullSync()
        } catch (e: Exception) {
            false
        }

        // Schedule periodic sync for future updates
        appSyncScheduler.schedulePeriodicSync()

        return success
    }
}