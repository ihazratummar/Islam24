package com.hazrat.islam24.core.domain.model.namesofallah

import com.hazrat.islam24.core.domain.model.namesofallah.Data

data class NamesDataModel(
    val code: Int,
    val `data`: List<com.hazrat.islam24.core.domain.model.namesofallah.Data>,
    val status: String
)