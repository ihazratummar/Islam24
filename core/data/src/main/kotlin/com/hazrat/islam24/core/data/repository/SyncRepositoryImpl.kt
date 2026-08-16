package com.hazrat.islam24.core.data.repository

import com.hazrat.database.dao.prayer.PrayerLogDao
import com.hazrat.database.dao.prayer.PrayerSettingDao
import com.hazrat.database.dao.quran.KhatamDao
import com.hazrat.database.dao.quran.QuranDao
import com.hazrat.datastore.SyncDatastore
import com.hazrat.datastore.TokenStorage
import com.hazrat.domain.repository.SyncRepository
import com.hazrat.islam24.core.data.mapper.toDto
import com.hazrat.islam24.core.data.mapper.toEntity
import com.hazrat.remote.api.sync.SyncApi
import com.hazrat.remote.dto.sync.SyncRequestDto
import timber.log.Timber

/**
 * Repository responsible for orchestrating bidirectional synchronization across
 * Prayer Logs, Prayer Settings, Khatam Plans, Quran Bookmarks, and Recent Surahs.
 *
 * @author hazratummar
 * Created on 14/08/26
 */
class SyncRepositoryImpl(
    private val syncApi: SyncApi,
    private val prayerLogDao: PrayerLogDao,
    private val syncDatastore: SyncDatastore,
    private val prayerSettingDao: PrayerSettingDao,
    private val tokenStorage: TokenStorage,
    private val khatamDao: KhatamDao,
    private val quranDao: QuranDao
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

        // 2. Gather local unsynced logs, settings & Quran data
        val lastSyncedAt = syncDatastore.getLastSyncedAt()
        val unsyncedLogs = prayerLogDao.getUnsyncedLogs()
        val localSetting = prayerSettingDao.getSettings()

        val unsyncedKhatams = khatamDao.getUnSynced()
        val unsyncedBookmarks = quranDao.getUnSyncedBookmarks()
        val unsyncedRecent = quranDao.getUnSyncedRecentSurah()

        val request = SyncRequestDto(
            lastSyncedAt = lastSyncedAt,
            prayerSettings = if (localSetting?.isSynced == false) localSetting.toDto() else null,
            prayerLogSyncDto = unsyncedLogs.map { it.toDto() },
            khatamPlans = unsyncedKhatams.map { it.toDto() },
            quranBookmarks = unsyncedBookmarks.map { it.toDto() },
            recentSurahs = unsyncedRecent.map { it.toDto() }
        )

        // 3. Call Unified Backend Endpoint
        val response = syncApi.syncData(request = request) ?: return false

        // 4. HARD DELETE confirmed deleted records from Room
        if (response.confirmedDeletedKhatamIds.isNotEmpty()) {
            khatamDao.deleteKhatamPlan(ids = response.confirmedDeletedKhatamIds)
        }
        if (response.confirmedDeletedBookmarkIds.isNotEmpty()) {
            quranDao.hardDeleteBookmark(ids = response.confirmedDeletedBookmarkIds)
        }
        if (response.confirmedDeletedRecentSurahNumbers.isNotEmpty()) {
            quranDao.deleteRecentSurah(surahNumbers = response.confirmedDeletedRecentSurahNumbers)
        }

        // 5. Mark pushed local items as synced
        if (response.syncedLogDate.isNotEmpty()) {
            prayerLogDao.markAsSynced(response.syncedLogDate)
        }
        if (unsyncedKhatams.isNotEmpty()) {
            khatamDao.markAsSynced(unsyncedKhatams.filter { !it.isDeleted }.map { it.id })
        }
        if (unsyncedBookmarks.isNotEmpty()) {
            quranDao.markBookmarkAsSynced(ids = unsyncedBookmarks.filter { !it.isDeleted }.map { it.id })
        }
        if (unsyncedRecent.isNotEmpty()) {
            quranDao.markRecentAsSynced(surahNumbers = unsyncedRecent.filter { !it.isDeleted }.map { it.surahNumber })
        }

        // 6. Merge incoming server updates into Room
        // 6.1 Prayer logs
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

        // 6.2 Prayer settings
        response.prayerSettings?.let { serverSetting ->
            prayerSettingDao.upsert(serverSetting.toEntity(isSynced = true))
        }

        // 6.3 Khatam Plans (Max Global Ayah Wins)
        for (serverPlan in response.khatamPlans) {
            val local = khatamDao.getById(serverPlan.id)
            if (local == null) {
                khatamDao.insertOrUpdateKhatamPlan(serverPlan.toEntity(isSynced = true))
            } else {
                val maxGlobal = maxOf(local.lastReadGlobalAyahNumber, serverPlan.lastReadGlobalAyahNumber)
                val winningSurah = if (serverPlan.lastReadGlobalAyahNumber >= local.lastReadGlobalAyahNumber) {
                    serverPlan.lastReadSurahNumber
                } else {
                    local.lastReadSurahNumber
                }
                val winningAyah = if (serverPlan.lastReadGlobalAyahNumber >= local.lastReadGlobalAyahNumber) {
                    serverPlan.lastReadAyahNumber
                } else {
                    local.lastReadAyahNumber
                }
                val winningStatus = if (serverPlan.status == "COMPLETED" || local.status == "COMPLETED") {
                    "COMPLETED"
                } else if (serverPlan.updatedTimestamp > local.updatedTimestamp) {
                    serverPlan.status
                } else {
                    local.status
                }
                val winningCompletedTimestamp = if (serverPlan.status == "COMPLETED") {
                    serverPlan.completedTimestamp
                } else {
                    local.completedTimestamp
                }
                khatamDao.insertOrUpdateKhatamPlan(
                    local.copy(
                        lastReadGlobalAyahNumber = maxGlobal,
                        lastReadSurahNumber = winningSurah,
                        lastReadAyahNumber = winningAyah,
                        completedAyahsCount = maxOf(local.completedAyahsCount, serverPlan.completedAyahsCount),
                        status = winningStatus,
                        completedTimestamp = winningCompletedTimestamp,
                        targetEndDateTimestamp = if (serverPlan.updatedTimestamp > local.updatedTimestamp) {
                            serverPlan.targetEndDateTimestamp
                        } else {
                            local.targetEndDateTimestamp
                        },
                        updatedTimestamp = maxOf(local.updatedTimestamp, serverPlan.updatedTimestamp),
                        isDeleted = serverPlan.isDeleted,
                        isSynced = true
                    )
                )
            }
        }

        // 6.4 Recent Surahs (Max Ayah Number Wins)
        for (serverRecent in response.recentSurahs) {
            val local = quranDao.getRecentSurahByNumber(serverRecent.surahNumber)
            if (local == null) {
                quranDao.insertRecentSurah(serverRecent.toEntity(isSynced = true))
            } else {
                quranDao.insertRecentSurah(
                    local.copy(
                        ayahNumber = maxOf(local.ayahNumber, serverRecent.ayahNumber),
                        formattedDate = if (serverRecent.timestamp >= local.timestamp) serverRecent.formattedDate else local.formattedDate,
                        timestamp = maxOf(local.timestamp, serverRecent.timestamp),
                        isDeleted = serverRecent.isDeleted,
                        isSynced = true
                    )
                )
            }
        }

        // 6.5 Quran Bookmarks (Upsert remote active bookmarks)
        for (serverBookmark in response.quranBookmarks) {
            quranDao.upsertBookmark(serverBookmark.toEntity(isSynced = true))
        }

        // 7. Persist new server sync timestamp
        syncDatastore.setLastSyncedAt(timestamp = response.syncedAt)
        return true
    }
}