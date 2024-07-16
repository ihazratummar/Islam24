package com.hazrat.islam24.core.network

import com.hazrat.islam24.core.domain.model.gregoriantohijri.GregorianToHijriResponse
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

/**
 * Interface representing the API service for converting Gregorian to Hijri dates.
 * This interface defines methods for retrieving Hijri date conversion data from an external API.
 */
@Singleton
interface GregorianToHijriApi {

    /**
     * Retrieves the Hijri date conversion data for the specified Gregorian date from the API.
     *
     * @param month The Gregorian date to be converted to Hijri, in the format "yyyy-MM-dd".
     * @return GregorianToHijriResponse containing the converted Hijri date data.
     */
    @GET("v1/gToH/{date}")
    suspend fun getGtoHDate(
        @Path("date") month: String
    ): com.hazrat.islam24.core.domain.model.gregoriantohijri.GregorianToHijriResponse
}
