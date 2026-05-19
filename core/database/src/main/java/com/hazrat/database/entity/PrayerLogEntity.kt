package com.hazrat.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * @author hazratummar
 * Created on 18/05/26
 */


@Entity(
    tableName = "prayer_logs",
    primaryKeys = ["date", "prayer"]
)
data class PrayerLogEntity(
    val date: String,
    val prayer: String,

    @ColumnInfo(name = "logged_at")
    val loggedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "sync_status")
    val syncStatus: String = SyncStatus.PENDING.name,

    @ColumnInfo(name = "is_deleted", defaultValue = "0")
    val isDeleted: Boolean = false

)

enum class SyncStatus { PENDING, SYNCED, FAILED }