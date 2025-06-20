package com.hazrat.zakat.screen.zakat.screen.zakat_screen

import com.hazrat.database.entity.zakat.ZakatEntity
import com.hazrat.model.DateType

data class ZakatScreenState(
    val zakatEntity: List<ZakatEntity> = emptyList(),
    val isZakatDetailsOpen: Boolean = false,
    val silverPrice: String = "",
    val nisabAmount: String = "0.0",
    val money: String = "0.0",
    val bankCash: String = "0.0",
    val gold: String = "0.0",
    val silver: String = "0.0",
    val tradeAmount: String = "0.0",
    val monthCost: String = "0.0",
    val debt: String = "0.0",

    val totalAsset: Double = 0.0,
    val zakatAmount: Double = 0.0,

    val sortType: DateType = DateType.DATE_DESC,
)
