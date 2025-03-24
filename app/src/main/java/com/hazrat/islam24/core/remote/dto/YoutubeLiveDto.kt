package com.hazrat.islam24.core.remote.dto

import kotlinx.serialization.Serializable

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-03-2025
 */

@Serializable
data class YoutubeLiveDto (
    @Serializable
    val etag: String,
    @Serializable
    val items: List<ItemDto>,
    @Serializable
    val kind: String,
)

@Serializable
data class ItemDto(
    @Serializable
    val etag: String,
    @Serializable
    val id: IdDto,
)

@Serializable
data class IdDto(
    @Serializable
    val kind: String,
    @Serializable
    val videoId: String,
)