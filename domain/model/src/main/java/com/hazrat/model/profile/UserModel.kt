package com.hazrat.model.profile

import kotlinx.datetime.Instant
import java.util.UUID


data class UserModel(
    val id: UUID,
    val googleId: String,
    val name: String,
    val email: String,
    val picture: String,
    val createdAt: Instant
)
