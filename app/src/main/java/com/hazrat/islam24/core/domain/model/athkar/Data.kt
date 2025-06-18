package com.hazrat.islam24.core.domain.model.athkar


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("morning")
    val morning: List<MorningAkhtarData>
)