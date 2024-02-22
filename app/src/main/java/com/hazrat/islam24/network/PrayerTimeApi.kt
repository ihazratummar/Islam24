//PrayerTimeApi.kt

package com.hazrat.islam24.network
import com.hazrat.islam24.domain.model.prayertime.prayertimemodel.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface PrayerTimeApi {
    @GET("{year}/{month}")
    suspend fun getPrayerTimes(
        @Path("year") date: Int,
        @Path("month") month: Int,
        @Query("latitude")latitude:String,
        @Query("longitude")longitude:String,
        @Query("method")method:Int,
        @Query("school")school: Int
    ): ApiResponse

}