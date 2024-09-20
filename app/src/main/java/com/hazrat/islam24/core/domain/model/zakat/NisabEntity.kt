package com.hazrat.islam24.core.domain.model.zakat

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class NisabEntity(
    val silverPrice: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1
)
