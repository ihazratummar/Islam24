package com.hazrat.remote.dto.sync

import com.hazrat.remote.dto.quran.KhatamPlanSyncDto
import com.hazrat.remote.dto.quran.QuranBookmarkSyncDto
import com.hazrat.remote.dto.quran.RecentSurahSyncDto
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class SyncRequestDto(
    val lastSyncedAt: Instant? = null,
    val prayerSettings : PrayerSettingSyncDto ? = null,
    val prayerLogSyncDto: List<PrayerLogSyncDto> = emptyList(),

    // Quran Sync
    val khatamPlans: List<KhatamPlanSyncDto> = emptyList(),
    val quranBookmarks: List<QuranBookmarkSyncDto> = emptyList(),
    val recentSurahs: List<RecentSurahSyncDto> = emptyList()
)
