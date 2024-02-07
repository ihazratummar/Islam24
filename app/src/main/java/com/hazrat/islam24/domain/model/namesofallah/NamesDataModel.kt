package com.hazrat.islam24.domain.model.namesofallah

import com.hazrat.islam24.domain.model.namesofallah.Data

data class NamesDataModel(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)