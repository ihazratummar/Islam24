package com.hazrat.islam24.core.network

import com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton


/**
 * Interface representing the API service for retrieving location names based on latitude and longitude.
 * This interface defines methods for fetching location names from an external API.
 */
@Singleton
interface LocationNameApi {

    /**
     * Retrieves the location name based on the provided latitude and longitude coordinates.
     *
     * @param format The format in which the location data should be returned (default is JSON).
     * @param lat The latitude coordinate of the location.
     * @param lon The longitude coordinate of the location.
     * @return LocationNameFinder containing the location name information.
     */
    @GET("reverse")
    suspend fun getLocationName(
        @Query("format") format: String = "json",
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
}
