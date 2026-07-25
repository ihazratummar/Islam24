package com.hazrat.zakat.zakat_list

import com.hazrat.database.entity.zakat.ZakatEntity

data class ZakatListState(
    val zakatEntityList: List<ZakatEntity> = emptyList(),
    val totalAsset: Double = 0.0,
    val zakatAmount: Double = 0.0,
    val gold: String = "",
    val silver: String = "",
    val money: String = "",
    val debt: String = "",
    val tradeAmount: String = "",
    val monthCost: String = "",
    val date: String = "",
    val isZakatDetailsOpen: Boolean = false
)
