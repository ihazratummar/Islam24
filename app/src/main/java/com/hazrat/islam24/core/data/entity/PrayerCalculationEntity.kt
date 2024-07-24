package com.hazrat.islam24.core.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_method_entity")
data class PrayerCalculationEntity(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "calculation_method")
    val method: Int? = 1
)

@Entity(tableName = "juristic_method_entity")
data class PrayerJuristicEntity(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "juristic_method")
    val school: Int? = 0 // Add school parameter
)


