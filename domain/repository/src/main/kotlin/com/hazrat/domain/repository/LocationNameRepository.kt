package com.hazrat.domain.repository

import com.hazrat.database.entity.LocationDetailsEntity
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
    suspend fun getLocationName(): String

    /**
     * Fetches the location name from a remote source.
     *
     * @return A string representing the fetched location name, or null if the fetch fails.
     */
    suspend fun fetchLocationName(): String?


    suspend fun locationName() : Flow<String>


}