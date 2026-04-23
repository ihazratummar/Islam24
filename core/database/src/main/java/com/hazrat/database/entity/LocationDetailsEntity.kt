package com.hazrat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_details")
data class LocationDetailsEntity(
    @PrimaryKey val id: Long = 1,
    val locationName: String = ""
)
