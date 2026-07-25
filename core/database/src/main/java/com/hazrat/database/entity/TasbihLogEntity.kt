package com.hazrat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasbih_log_table")
data class TasbihLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tasbihId: Int,
    val countAdded: Int,
    val dateString: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis()
)
