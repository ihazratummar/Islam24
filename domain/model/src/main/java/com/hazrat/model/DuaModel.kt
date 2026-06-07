package com.hazrat.model

import androidx.compose.runtime.Stable


@Stable
data class DuaCategoryModel(
    val id: Int,
    val title: String
)


@Stable
data class DuaItemModel(
    val id: Int,
    val categoryId: Int,
    val arabicText: String,
    val translation: String,
    val reference: String,
    val repeatCount: Int
)

