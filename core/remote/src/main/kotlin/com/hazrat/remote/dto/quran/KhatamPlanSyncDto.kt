package com.hazrat.remote.dto.quran

import kotlinx.serialization.Serializable

/**
 * @author hazratummar
 * Created on 16/08/26
 */


@Serializable
data class KhatamPlanSyncDto(
    val id: String,
    val title: String = "Khatam Quran",
    val startDateTimestamp: Long,
    val targetEndDateTimestamp: Long,
    val lastReadSurahNumber: Int = 1,
    val lastReadAyahNumber: Int = 1,
    val lastReadGlobalAyahNumber: Int = 1,
    val completedAyahsCount: Int = 0,
    val status: String = "IN_PROGRESS",
    val completedTimestamp: Long? = null,
    val isDeleted: Boolean = false,
    val updatedTimestamp: Long
)