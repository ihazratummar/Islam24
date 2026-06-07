//PrayerTimeApi.kt

package com.hazrat.remote.api
import com.hazrat.remote.dto.NewPrayerTimeDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Interface representing the API service for retrieving prayer times.
 * This interface defines methods for fetching prayer times from an external API.
 */

interface PrayerTimeApi {

    /**
     * Retrieves prayer times for a specific year and month from the API.
     *
     * @param year The year for which prayer times are requested.
     * @param latitude The latitude coordinate of the location.
     * @param longitude The longitude coordinate of the location.
     * @param method The calculation method for prayer times.
     * @param school The calculation school for prayer times.
     * @return ApiResponse containing the prayer times data.
     */

    @GET("{year}")
    suspend fun newPrayerTimesRequest(
        @Path("year") year: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("method") method: Int,
        @Query("school") school: Int
    ): NewPrayerTimeDto
}
