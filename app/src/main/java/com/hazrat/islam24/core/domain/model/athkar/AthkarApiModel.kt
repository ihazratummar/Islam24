package com.hazrat.islam24.core.domain.model.athkar


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AthkarApiModel(
    @SerialName("data")
    val `data`: List<Data>,
    @SerialName("status")
    val status: String
)