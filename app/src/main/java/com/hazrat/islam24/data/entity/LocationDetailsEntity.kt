package com.hazrat.islam24.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_details")
data class LocationDetailsEntity(
    @PrimaryKey val id: Long = 1,
    val village: String?,
    val city: String?,
    val town: String?,
    val suburb: String?
)
