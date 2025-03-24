package com.hazrat.islam24.core.data.repository

import com.hazrat.islam24.core.data.mapper.hajjlive.toDomain
import com.hazrat.islam24.core.domain.model.hajjlive.HajjLiveYoutubeModel
import com.hazrat.islam24.core.domain.repository.HajjLiveRepository
import com.hazrat.islam24.core.remote.api.GoogleCloudConsoleApi

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */

class HajjLiveRepositoryImpl (
    private val api: GoogleCloudConsoleApi
): HajjLiveRepository {

    override suspend fun getHajjLiveVideoId(): HajjLiveYoutubeModel {
        val response = api.getYoutubeChannelId(
            part = "snippet",
            channelId = "UCraPI8sg-eiNzUrurxhKeEQ",
            type = "video",
            eventType = "live",
            key = "AIzaSyBT0y6-xFdGX5zzjg8Df9clCTHOzuLh_7Y"
        )

        if (response.isSuccessful){
            return response.body().let { it?.toDomain() ?: HajjLiveYoutubeModel("", emptyList(), "") }
        }else{
            throw Exception("Something went wrong")
        }
    }
}