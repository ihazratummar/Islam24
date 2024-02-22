package com.hazrat.islam24.network

import com.hazrat.islam24.domain.model.locationmodel.LocationNameFinder
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface LocationNameApi {

    @GET("reverse")
    suspend fun getLocationName(
        @Query("format") format: String = "json",
        @Query("lat") lat: Double,
        @Query("lon") lon:Double
    ): LocationNameFinder
}