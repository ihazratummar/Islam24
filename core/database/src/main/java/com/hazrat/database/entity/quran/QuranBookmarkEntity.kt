package com.hazrat.database.entity.quran

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "quran_bookmark",
    foreignKeys = [
        ForeignKey(
            entity = AyahEntity::class,
            parentColumns = ["globalAyahNumber"],
            childColumns = ["globalAyahNumber"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("globalAyahNumber"),
        Index(value = ["surahNumber", "ayahNumber"]),
    ]
)
data class QuranBookmarkEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val surahNumber: Int,
    val ayahNumber: Int,
    val globalAyahNumber: Int,
    val isDeleted: Boolean = false,
    val isSynced: Boolean = false,
    val updatedAt: Long = 0
)
