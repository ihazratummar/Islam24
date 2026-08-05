package com.hazrat.database.entity.profile

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import java.util.UUID

@Entity(
    tableName = "users",
    indices = [Index(value = ["googleId"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    val id: UUID,
    val googleId: String,
    val name: String,
    val email: String,
    val picture: String,
    val createdAt: Instant
)
