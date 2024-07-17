package com.hazrat.islam24.core.domain.model.namesofallah

data class NamesDataModel(
    val code: Int,
    val `data`: List<com.hazrat.islam24.core.domain.model.namesofallah.NameOfAllahData>,
    val status: String
)