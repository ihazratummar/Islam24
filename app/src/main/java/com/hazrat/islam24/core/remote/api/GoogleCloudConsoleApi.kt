package com.hazrat.islam24.core.remote.api

import com.hazrat.islam24.core.remote.dto.YoutubeLiveDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */

@Singleton
interface GoogleCloudConsoleApi {

    @GET("search")
    suspend fun getYoutubeChannelId(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("type") type: String,
        @Query("eventType") eventType: String,
        @Query("key") key: String
    ): Response<YoutubeLiveDto>

}