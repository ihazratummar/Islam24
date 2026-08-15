package com.hazrat.islam24.core.data.repository

import com.hazrat.database.dao.prayer.PrayerLogDao
import com.hazrat.database.dao.prayer.PrayerSettingDao
import com.hazrat.datastore.SyncDatastore
import com.hazrat.datastore.TokenStorage
import com.hazrat.domain.repository.SyncRepository
import com.hazrat.islam24.core.data.mapper.toDto
import com.hazrat.islam24.core.data.mapper.toEntity
import com.hazrat.remote.api.sync.SyncApi
import com.hazrat.remote.dto.sync.SyncRequestDto
import timber.log.Timber


/**
 * @author hazratummar
 * Created on 14/08/26
 */

class SyncRepositoryImpl(
    private val syncApi: SyncApi,
    private val prayerLogDao: PrayerLogDao,
    private val syncDatastore: SyncDatastore,
    private val prayerSettingDao: PrayerSettingDao,
    private val tokenStorage: TokenStorage
) : SyncRepository {

    companion object {
        private const val TAG = "SYNC_REPOSITORY"
    }

    override suspend fun performFullSync(): Boolean {
        // 1. Check if user is logged in before attempting network sync
        val token = tokenStorage.getAccessToken()

        if (token.isNullOrBlank()) {
            Timber.tag(TAG).e("Skipping sync: User is not logged in.")
            return true // Not an error, just skip for guest users
        }

        // 2. Gather local unsynced logs & settings
        val lastSyncedAt = syncDatastore.getLastSyncedAt()
        val unsyncedLogs = prayerLogDao.getUnsyncedLogs()
        val localSetting = prayerSettingDao.getSettings()

        val request = SyncRequestDto(
            lastSyncedAt = lastSyncedAt,
            prayerSettings = if (localSetting?.isSynced == false) localSetting.toDto() else null,
            prayerLogSyncDto = unsyncedLogs.map { it.toDto() }
        )

        // 3. Call Unified Backend Endpoint
        val response = syncApi.syncData(request = request) ?: return false

        // 4. Mark pushed local items as synced
        if (response.syncedLogDate.isNotEmpty()){
            prayerLogDao.markAsSynced(response.syncedLogDate)
        }

        //5. Merge incoming server updates into database into room
        // For Prayer logs
        for (serverLog in response.prayerLogs) {
            val existing = prayerLogDao.getLogByDate(serverLog.logDate)
            if (existing == null) {
                prayerLogDao.upsert(serverLog.toEntity(isSynced = true))
            } else {
                val merged = existing.copy(
                    fajr = existing.fajr || serverLog.fajr,
                    dhuhr = existing.dhuhr || serverLog.dhuhr,
                    asr = existing.asr || serverLog.asr,
                    maghrib = existing.maghrib || serverLog.maghrib,
                    isha = existing.isha || serverLog.isha,
                    isSynced = true,
                    updatedAt = if (serverLog.updatedAt > existing.updatedAt) serverLog.updatedAt else existing.updatedAt
                )
                prayerLogDao.upsert(log = merged)
            }
        }

        // 6. Update winning settings
        response.prayerSettings?.let { serverSetting ->
            prayerSettingDao.upsert(serverSetting.toEntity(isSynced = true))
        }

        syncDatastore.setLastSyncedAt(timestamp = response.syncedAt)
        return true
    }
}