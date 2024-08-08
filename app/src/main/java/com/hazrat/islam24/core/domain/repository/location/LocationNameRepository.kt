package com.hazrat.islam24.core.domain.repository.location

import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface LocationNameRepository {

    /**
     * Retrieves the current location name.
     *
     * @return LocationNameFinder object representing the current location name.
     */
    suspend fun getLocationName(): LocationNameFinder

    /**
     * Fetches the location name from a remote source.
     *
     * @return A string representing the fetched location name, or null if the fetch fails.
     */
    suspend fun fetchLocationName(): String?

    /**
     * Retrieves the details of all saved locations.
     *
     * @return A Flow representing the list of LocationDetailsEntity objects containing location details.
     */
    fun getLocationDetails(): Flow<List<LocationDetailsEntity>>
}
