package com.hazrat.islam24.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationNameDao {

    /**
     * Retrieves the location details from the database.
     *
     * @return Flow representing the list of location details as LocationDetailsEntity objects.
     */
    @Query("SELECT * FROM location_details WHERE id = 1")
    fun getLocationDetails(): Flow<List<LocationDetailsEntity>>

    /**
     * Saves the given location details into the database.
     * If a location with the same ID already exists, it will be replaced.
     *
     * @param location The LocationDetailsEntity object to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocationDetails(location: LocationDetailsEntity)

    /**
     * Deletes all location details except for the one with ID equal to 1.
     * This method is useful for cleaning up the database.
     */
    @Query("DELETE FROM location_details WHERE id != 1")
    suspend fun deleteOtherLocationDetails()
}