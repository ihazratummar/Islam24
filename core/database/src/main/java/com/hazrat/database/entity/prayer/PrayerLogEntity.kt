package com.hazrat.database.entity.prayer

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate


/**
 * @author hazratummar
 * Created on 18/05/26
 */


@Entity(tableName = "prayer_logs")
data class PrayerLogEntity(
    @PrimaryKey
    val logDate: LocalDate,
    val fajr: Boolean = false,
    val dhuhr: Boolean = false,
    val asr: Boolean = false,
    val maghrib: Boolean = false,
    val isha: Boolean = false,
    val isSynced: Boolean = false,
    val updatedAt: Instant

)

enum class SyncStatus { PENDING, SYNCED, FAILED }