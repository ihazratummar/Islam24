package com.hazrat.database.entity.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "user_support_status")
data class UserSupportStatusEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val isSupporter: Boolean,
    val totalContributionUsd: Double,   // Combined USD total
    val totalContributionLocal: Double, // Combined local currency total
    val localCurrency: String?,              // Code (e.g., "INR")
    val totalSupporter: Int
)
