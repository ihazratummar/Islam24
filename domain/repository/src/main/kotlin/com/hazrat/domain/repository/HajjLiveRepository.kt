package com.hazrat.domain.repository

import com.hazrat.model.hajjlive.HajjLiveYoutubeModel

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */

interface HajjLiveRepository {

    suspend fun getHajjLiveVideoId(): HajjLiveYoutubeModel

}