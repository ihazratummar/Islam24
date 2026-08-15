package com.hazrat.remote.dto.sync

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class SyncRequestDto(
    val lastSyncedAt: Instant? = null,
    val prayerSettings : PrayerSettingSyncDto ? = null,
    val prayerLogSyncDto: List<PrayerLogSyncDto> = emptyList()
)
