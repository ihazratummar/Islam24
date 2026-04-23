package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.database.dao.LocationDao
import com.hazrat.database.dao.LocationNameDao
import com.hazrat.database.entity.LocationDetailsEntity
import com.hazrat.database.entity.LocationEntity


@Database(entities = [LocationEntity::class , LocationDetailsEntity::class], version = 5, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {

    /**
     * Retrieves the Data Access Object (DAO) for accessing basic location information.
     *
     * @return The LocationDao instance.
     */
    abstract fun locationDao(): LocationDao

    /**
     * Retrieves the Data Access Object (DAO) for accessing detailed location data.
     *
     * @return The LocationNameDao instance.
     */
    abstract fun locationNameDao(): LocationNameDao
}