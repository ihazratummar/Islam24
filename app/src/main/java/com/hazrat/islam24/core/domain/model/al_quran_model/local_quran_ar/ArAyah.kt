package com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar

data class ArAyah(
    val hizbQuarter: Int,
    val juz: Int,
    val manzil: Int,
    val number: Int,
    val numberInSurah: Int,
    val page: Int,
    val ruku: Int,
//    val sajda: Boolean,
    val text: String
)