package com.hazrat.model

import androidx.compose.runtime.Stable


@Stable
data class DuaCategoryModel(
    val id: Int,
    val title: String,
    val bnTitle: String? = null
)

@Stable
data class DuaChapterWithCountModel(
    val id: Int,
    val title: String,
    val bnTitle: String? = null,
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
    val bnTranslation: String? = null,
    val bnTransliteration: String? = null,
    val reference: String,
    val bnReference: String? = null,
    val repeatCount: Int,
    val audioUrl: String? = null,
    val isBookmarked: Boolean = false
)

@Stable
data class RecentReadDua(
    val chapterId: Int,
    val title: String,
    val bnTitle: String? = null,
    val duaCount: Int,
    val formattedDate: String,
    val timestamp: Long = System.currentTimeMillis()
)

