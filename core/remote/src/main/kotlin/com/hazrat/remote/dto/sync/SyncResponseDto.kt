package com.hazrat.remote.dto.sync

import com.hazrat.remote.dto.quran.KhatamPlanSyncDto
import com.hazrat.remote.dto.quran.QuranBookmarkSyncDto
import com.hazrat.remote.dto.quran.RecentSurahSyncDto
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SyncResponseDto(
    val syncedAt: Instant,
    val prayerSettings: PrayerSettingSyncDto ? = null,
    val prayerLogs: List<PrayerLogSyncDto> = emptyList(),
    val syncedLogDate: List<LocalDate> = emptyList(),

    // Quran Remote Updates
    val khatamPlans: List<KhatamPlanSyncDto> = emptyList(),
    val quranBookmarks: List<QuranBookmarkSyncDto> = emptyList(),
    val recentSurahs: List<RecentSurahSyncDto> = emptyList(),
    // Deletion ACKs (For Android Room to permanently Hard Delete)
    val confirmedDeletedKhatamIds: List<String> = emptyList(),
    val confirmedDeletedBookmarkIds: List<String> = emptyList(),
    val confirmedDeletedRecentSurahNumbers: List<Int> = emptyList()
)
