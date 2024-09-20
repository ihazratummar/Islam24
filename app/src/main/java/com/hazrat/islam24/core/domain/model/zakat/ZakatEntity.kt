package com.hazrat.islam24.core.domain.model.zakat

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ZakatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val money: Double,
    val gold: Double,
    val silver: Double,
    val tradeAmount: Double,
    val monthCost: Double,
    val debt: Double,
    val totalAsset: Double,
    val zakatAmount: Double
)
