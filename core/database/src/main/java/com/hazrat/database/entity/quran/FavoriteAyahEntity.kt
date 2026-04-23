package com.hazrat.database.entity.quran

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_ayah")
data class FavoriteAyahEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val surahNumber : Int,
    val ayahNumber: Int,

    val createdAt : Long = System.currentTimeMillis()
)
