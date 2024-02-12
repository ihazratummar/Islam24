package com.hazrat.islam24.data.location.coordinents

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_data")
data class LocationEntity(
    @PrimaryKey val id: Int = 1, // Assuming only one location entry, so id is constant
    val latitude: Double,
    val longitude: Double
)