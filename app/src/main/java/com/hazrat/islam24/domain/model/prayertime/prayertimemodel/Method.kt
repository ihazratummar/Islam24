package com.hazrat.islam24.domain.model.prayertime.prayertimemodel

data class Method(
    val id: Int,
    val location: Location,
    val name: String,
    val params: Params
)