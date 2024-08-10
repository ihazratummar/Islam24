package com.hazrat.islam24.core.domain.repository.location

import android.location.Location
import androidx.lifecycle.LiveData
import com.hazrat.islam24.core.data.entity.LocationEntity

/**
 * @author Hazrat Ummar Shaikh
 */

interface LocationRepository {

    /**
     * LiveData object that holds the current location.
     */
    val currentLocation: LiveData<Location>

    /**
     * Saves the specified location coordinates (latitude and longitude).
     *
     * @param latitude The latitude of the location to be saved.
     * @param longitude The longitude of the location to be saved.
     */
    suspend fun saveLocation(latitude: Double, longitude: Double)

    /**
     * Retrieves the saved location entity from the database.
     *
     * @return A LocationEntity object representing the saved location, or null if no location is saved.
     */
    suspend fun getLocation(): LocationEntity?
}