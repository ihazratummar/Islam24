//PrayerTimeApi.kt

package com.hazrat.islam24.core.network
import com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.ApiResponse
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
     * @param date The year for which prayer times are requested.
     * @param month The month for which prayer times are requested.
     * @param latitude The latitude coordinate of the location.
     * @param longitude The longitude coordinate of the location.
     * @param method The calculation method for prayer times.
     * @param school The calculation school for prayer times.
     * @return ApiResponse containing the prayer times data.
     */
    @GET("{year}/{month}")
    suspend fun getPrayerTimes(
        @Path("year") date: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("method") method: Int,
        @Query("school") school: Int
    ): com.hazrat.islam24.core.domain.model.prayertime.prayertimemodel.ApiResponse
}
