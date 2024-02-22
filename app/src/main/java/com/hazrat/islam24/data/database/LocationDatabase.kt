package com.hazrat.islam24.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.data.dao.LocationDao
import com.hazrat.islam24.data.entity.LocationDetailsEntity
import com.hazrat.islam24.data.dao.LocationNameDao
import com.hazrat.islam24.data.entity.LocationEntity


@Database(entities = [LocationEntity::class , LocationDetailsEntity::class],version = 3, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    abstract fun locationNameDao(): LocationNameDao

}