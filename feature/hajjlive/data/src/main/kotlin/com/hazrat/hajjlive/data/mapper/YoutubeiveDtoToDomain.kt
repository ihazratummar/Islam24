package com.hazrat.hajjlive.data.mapper

import com.hazrat.model.hajjlive.HajjLiveYoutubeModel
import com.hazrat.remote.dto.IdDto
import com.hazrat.remote.dto.ItemDto
import com.hazrat.remote.dto.YoutubeLiveDto

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */

fun YoutubeLiveDto.firstToDomainOrNull() : HajjLiveYoutubeModel? {
    return items.firstOrNull()?.let {
        HajjLiveYoutubeModel(videoId = it.id.videoId)
    }
}