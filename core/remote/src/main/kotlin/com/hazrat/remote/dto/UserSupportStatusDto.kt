package com.hazrat.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserSupportStatusDto(
    val isSupporter: Boolean,
    val totalContributionUsd: Double,   // Combined USD total
    val totalContributionLocal: Double, // Combined local currency total
    val localCurrency: String?,              // Code (e.g., "INR")
    val totalSupporter: Int
)