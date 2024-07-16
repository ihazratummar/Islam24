package com.hazrat.islam24.core.network

import com.hazrat.hijricaneldar.domain.model.hijricalendar.HijriCalendarResponse
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton


/**
 * Interface representing the API service for retrieving Hijri calendar data.
 * This interface defines methods for fetching Hijri calendar data from an external API.
 */
@Singleton
interface HijriCalendarApi {

    /**
     * Retrieves the Hijri calendar data for the specified month and year from the API.
     *
     * @param month The month for which the Hijri calendar data is requested.
     * @param year The year for which the Hijri calendar data is requested.
     * @return HijriCalendarResponse containing the Hijri calendar data for the specified month and year.
     */
    @GET("v1/hToGCalendar/{month}/{year}")
    suspend fun getHijriCalendar(
        @Path("month") month: Int,
        @Path("year") year: String
    ): HijriCalendarResponse
}
