package com.hazrat.hajjlive.data.repository

import com.hazrat.domain.repository.HajjLiveRepository
import com.hazrat.hajjlive.data.mapper.firstToDomainOrNull
import com.hazrat.model.hajjlive.HajjLiveYoutubeModel
import com.hazrat.remote.api.GoogleCloudConsoleApi

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */

class HajjLiveRepositoryImpl(
    private val api: GoogleCloudConsoleApi
) : HajjLiveRepository {

    override suspend fun getHajjLiveVideoId(): HajjLiveYoutubeModel {
        val response = api.getYoutubeChannelId(
            part = "snippet",
            channelId = "UCraPI8sg-eiNzUrurxhKeEQ",
            type = "video",
            eventType = "live",
            key = "AIzaSyBT0y6-xFdGX5zzjg8Df9clCTHOzuLh_7Y"
        )

        if (response.isSuccessful) {
            return response.body()?.firstToDomainOrNull() ?: throw Exception("Something went wrong")
        } else {
            throw Exception("Something went wrong")
        }
    }
}