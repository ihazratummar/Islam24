package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.core.domain.model.hajjlive.HajjLiveYoutubeModel

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */

interface HajjLiveRepository {

    suspend fun getHajjLiveVideoId(): HajjLiveYoutubeModel

}