package com.hazrat.islam24.core.data.mapper.hajjlive

import com.hazrat.islam24.core.domain.model.hajjlive.HajjLiveYoutubeModel
import com.hazrat.islam24.core.domain.model.hajjlive.Id
import com.hazrat.islam24.core.domain.model.hajjlive.Item
import com.hazrat.islam24.core.remote.dto.IdDto
import com.hazrat.islam24.core.remote.dto.ItemDto
import com.hazrat.islam24.core.remote.dto.YoutubeLiveDto

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */

fun YoutubeLiveDto.toDomain() : HajjLiveYoutubeModel {
    return HajjLiveYoutubeModel(
        etag = this.etag,
        items = this.items.toDomainList(),
        kind = this.kind
    )
}


fun ItemDto.toDomain(): Item{
    return Item(
        id = this.id.toDomain(),
        etag = this.etag
    )
}

fun List<ItemDto>.toDomainList() : List<Item> {
    return this.map { it.toDomain() }
}

fun IdDto.toDomain() : Id {
    return Id(
        kind = this.kind,
        videoId = this.videoId
    )
}