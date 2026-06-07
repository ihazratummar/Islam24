package com.hazrat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "dua_category")
data class DuaCategoryEntity(
    @PrimaryKey val id: Int,
    val title: String,
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
    val reference: String,
    val repeatCount: Int,
    val audioUrl: String?,
    val isBookmarked: Int
)
