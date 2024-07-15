package com.hazrat.islam24.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.data.dao.LocationDao
import com.hazrat.islam24.data.entity.LocationDetailsEntity
import com.hazrat.islam24.data.dao.LocationNameDao
import com.hazrat.islam24.data.entity.LocationEntity


@Database(entities = [LocationEntity::class , LocationDetailsEntity::class], version = 4, exportSchema = false)
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