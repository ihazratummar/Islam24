package com.hazrat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_details")
data class LocationDetailsEntity(
    @PrimaryKey
    val id: Long = 1,

    val locationName: String = "Location",

    val latitude : Double,
    val longitude: Double,

    val updatedAt: Long = System.currentTimeMillis(),

    val lastPrayerSyncAt: Long = 0L
)
