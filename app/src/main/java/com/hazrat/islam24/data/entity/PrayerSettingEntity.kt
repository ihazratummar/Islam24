package com.hazrat.islam24.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "method_entity")
data class PrayerSettingEntity(
    @PrimaryKey
    @ColumnInfo(name = "method")
    val method: Int?,

    @ColumnInfo(name = "school")
    val school: Int? // Add school parameter
)
