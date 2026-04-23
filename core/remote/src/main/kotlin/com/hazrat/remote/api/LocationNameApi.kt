package com.hazrat.remote.api

import com.hazrat.remote.dto.LocationNameDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Interface representing the API service for retrieving location names based on latitude and longitude.
 * This interface defines methods for fetching location names from an external API.
 */

interface LocationNameApi {

    /**
     * Retrieves the location name based on the provided latitude and longitude coordinates.
     *
     * @param format The format in which the location data should be returned (default is JSON).
     * @param lat The latitude coordinate of the location.
     * @param lon The longitude coordinate of the location.
     * @return LocationNameDto containing the location name information.
     */
    @GET("reverse")
    suspend fun getLocationName(
        @Query("key") key: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "json",
    ): Response<LocationNameDto>
}
