package com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar

data class LocalQuranModelArItem(
    val ayahs: List<ArAyah>,
    val englishName: String,
    val englishNameTranslation: String,
    val name: String,
    val number: Int,
    val revelationType: String
)