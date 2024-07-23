package com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel

data class Method(
    val id: Int,
    val location: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Location,
    val name: String,
    val params: com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.Params
)