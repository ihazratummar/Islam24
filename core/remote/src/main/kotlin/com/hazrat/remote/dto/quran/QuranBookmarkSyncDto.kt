package com.hazrat.remote.dto.quran

import kotlinx.serialization.Serializable


/**
 * @author hazratummar
 * Created on 16/08/26
 */

@Serializable
data class QuranBookmarkSyncDto(
    val id: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val globalAyahNumber: Int,
    val isDeleted: Boolean = false,
    val updatedAt: Long
)