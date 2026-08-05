package com.hazrat.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class LiveCommunityTickerPayload(
    val eventId: String,
    val donorName: String,
    val avatarUrl: String? = null,
    val type: String,
    val amount: Double? = null,
    val currency: String ? = null,
    val productId: String ? = null,
    val createdAt: Instant? = null
)
