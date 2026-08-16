package com.hazrat.remote.dto.quran

import kotlinx.serialization.Serializable

/**
 * @author hazratummar
 * Created on 16/08/26
 */


@Serializable
data class RecentSurahSyncDto(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val formattedDate: String,
    val isDeleted: Boolean = false,
    val timestamp: Long
)
