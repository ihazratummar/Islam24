package com.hazrat.database.entity.zakat

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class NisabEntity(
    @PrimaryKey
    val id: Int = 1,
    val silverPrice: Double = 0.0
)
