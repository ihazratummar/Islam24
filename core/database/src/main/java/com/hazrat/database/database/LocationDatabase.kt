package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.database.dao.LocationNameDao
import com.hazrat.database.entity.LocationDetailsEntity
import com.hazrat.database.entity.LocationEntity


@Database(entities = [LocationDetailsEntity::class], version = 7, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {


    /**
     * Retrieves the Data Access Object (DAO) for accessing detailed location data.
     *
     * @return The LocationNameDao instance.
     */
    abstract fun locationNameDao(): LocationNameDao
}