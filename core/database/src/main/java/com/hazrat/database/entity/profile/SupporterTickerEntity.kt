package com.hazrat.database.entity.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing cached community supporter tickers (max 10 recent records).
 * @author Hazrat Ummar Shaikh
 */
@Entity(tableName = "supporter_ticker")
data class SupporterTickerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val donorName: String,
    val type: String,
    val amount: Double?,
    val currency: String?,
    val timestamp: Long = System.currentTimeMillis()
)
