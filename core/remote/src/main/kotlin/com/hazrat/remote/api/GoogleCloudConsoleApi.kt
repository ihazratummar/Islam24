package com.hazrat.remote.api

import com.hazrat.remote.dto.YoutubeLiveDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */


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