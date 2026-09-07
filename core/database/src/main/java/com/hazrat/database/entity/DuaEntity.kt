package com.hazrat.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "dua_category")
data class DuaCategoryEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val bnTitle: String? = null,
    val audioUrl: String?
)

@Entity(
    tableName = "dua_item",
    foreignKeys = [
        ForeignKey(
            entity = DuaCategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"], name = "index_dua_categoryId")]
)
data class DuaItemEntity(
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val arabicText: String,
    val translation: String,
    val transliteration: String,
    val bnTranslation: String? = null,
    val bnTransliteration: String? = null,
    val reference: String,
    val bnReference: String? = null,
    val repeatCount: Int,
    val audioUrl: String?,
    val isBookmarked: Int
)

@Entity(tableName = "recent_dua")
data class RecentDuaEntity(
    @PrimaryKey val chapterId: Int,
    val title: String,
    val bnTitle: String? = null,
    val duaCount: Int,
    val formattedDate: String,
    val timestamp: Long = System.currentTimeMillis()
)
