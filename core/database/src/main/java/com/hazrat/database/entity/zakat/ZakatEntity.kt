package com.hazrat.database.entity.zakat

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity
data class ZakatEntity(
    @PrimaryKey
    val id: String= UUID.randomUUID().toString(),
    val date: Long,
    val money: Double,
    val gold: Double,
    val silver: Double,
    val tradeAmount: Double,
    val monthCost: Double,
    val debt: Double,
    val totalAsset: Double,
    val zakatAmount: Double
) {
    // No-argument constructor required for Firestore deserialization
    constructor() : this(id = "", date = 0L, money = 0.0, gold = 0.0, silver = 0.0, tradeAmount = 0.0, monthCost = 0.0, debt = 0.0, totalAsset = 0.0, zakatAmount = 0.0)
}
