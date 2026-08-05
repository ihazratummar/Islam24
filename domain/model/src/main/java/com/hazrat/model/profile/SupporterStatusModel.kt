package com.hazrat.model.profile

data class SupporterStatusModel(
    val isSupporter: Boolean,
    val totalContributionUsd: Double,   // Combined USD total
    val totalContributionLocal: Double, // Combined local currency total
    val localCurrency: String?,              // Code (e.g., "INR")
    val totalSupporter: Int
)
