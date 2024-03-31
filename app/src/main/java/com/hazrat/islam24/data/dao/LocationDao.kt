package com.hazrat.islam24.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.data.entity.LocationEntity


@Dao
interface LocationDao {

    /**
     * Retrieves the location data from the database.
     *
     * @return The location data as a LocationEntity object, or null if not found.
     */
    @Query("SELECT * FROM location_data WHERE id = 1")
    suspend fun getLocation(): LocationEntity?

    /**
     * Saves the given location data into the database.
     * If a location with the same ID already exists, it will be replaced.
     *
     * @param location The LocationEntity object to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(location: LocationEntity)
}