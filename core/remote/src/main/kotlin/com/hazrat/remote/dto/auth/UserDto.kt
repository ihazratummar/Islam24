package com.hazrat.remote.dto.auth

import com.hazrat.remote.serializer.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserDto(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val googleId: String,
    val name: String,
    val email: String,
    val picture: String,
    val createdAt: Instant
)
