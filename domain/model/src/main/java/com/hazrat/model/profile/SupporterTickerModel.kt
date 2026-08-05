package com.hazrat.model.profile

data class SupporterTickerModel(
    val eventId: String,
    val donorName: String,
    val type : String,
    val amount: Double?,
    val currency: String?
)
