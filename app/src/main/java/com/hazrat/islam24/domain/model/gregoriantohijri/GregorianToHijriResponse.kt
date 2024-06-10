package com.hazrat.islam24.domain.model.gregoriantohijri

import com.hazrat.hijricaneldar.domain.model.gregoriantohijri.Data

data class GregorianToHijriResponse(
    val code: Int,
    val `data`: Data,
    val status: String
)