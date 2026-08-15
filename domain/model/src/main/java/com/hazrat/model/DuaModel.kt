package com.hazrat.model

import androidx.compose.runtime.Stable


@Stable
data class DuaCategoryModel(
    val id: Int,
    val title: String
)

@Stable
data class DuaChapterWithCountModel(
    val id: Int,
    val title: String,
    val audioUrl: String? = null,
    val duaCount: Int = 0
)

@Stable
data class DuaItemModel(
    val id: Int,
    val categoryId: Int,
    val arabicText: String,
    val translation: String,
    val transliteration: String = "",
    val reference: String,
    val repeatCount: Int,
    val audioUrl: String? = null,
    val isBookmarked: Boolean = false
)

@Stable
data class RecentReadDua(
    val chapterId: Int,
    val title: String,
    val duaCount: Int,
    val formattedDate: String,
    val timestamp: Long = System.currentTimeMillis()
)

