//PrayerTimeApi.kt

package com.hazrat.islam24.core.remote.api
import com.hazrat.islam24.core.remote.dto.NewPrayerTimeDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


/**
 * Interface representing the API service for retrieving prayer times.
 * This interface defines methods for fetching prayer times from an external API.
 */
@Singleton
interface PrayerTimeApi {

    /**
     * Retrieves prayer times for a specific year and month from the API.
     *
     * @param year The year for which prayer times are requested.
     * @param month The month for which prayer times are requested.
     * @param latitude The latitude coordinate of the location.
     * @param longitude The longitude coordinate of the location.
     * @param method The calculation method for prayer times.
     * @param school The calculation school for prayer times.
     * @param annual The calculation Annual Boolean Value for prayer times full year or if false 1 month.
     * @return ApiResponse containing the prayer times data.
     */

    @GET("{year}/{month}")
    suspend fun newPrayerTimesRequest(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("method") method: Int,
        @Query("school") school: Int,
        @Query("annual") annual: Boolean
    ): NewPrayerTimeDto
}
