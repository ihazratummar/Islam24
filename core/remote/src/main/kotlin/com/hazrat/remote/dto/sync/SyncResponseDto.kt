package com.hazrat.remote.dto.sync

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SyncResponseDto(
    val syncedAt: Instant,
    val prayerSettings: PrayerSettingSyncDto ? = null,
    val prayerLogs: List<PrayerLogSyncDto> = emptyList(),
    val syncedLogDate: List<LocalDate> = emptyList()
)
