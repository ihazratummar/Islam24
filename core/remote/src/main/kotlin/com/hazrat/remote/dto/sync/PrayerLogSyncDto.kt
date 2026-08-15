package com.hazrat.remote.dto.sync

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable


@Serializable
data class PrayerLogSyncDto(
    val logDate: LocalDate,
    val fajr: Boolean,
    val dhuhr: Boolean,
    val asr: Boolean,
    val maghrib: Boolean,
    val isha: Boolean,
    val updatedAt: Instant
)
