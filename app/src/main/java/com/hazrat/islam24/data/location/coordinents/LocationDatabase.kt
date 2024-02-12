package com.hazrat.islam24.data.location.coordinents

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.data.location.locationdetails.LocationDetailsEntity
import com.hazrat.islam24.data.location.locationdetails.LocationNameDao


@Database(entities = [LocationEntity::class , LocationDetailsEntity::class],version = 2, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    abstract fun locationNameDao(): LocationNameDao

}