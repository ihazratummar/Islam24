package com.hazrat.database.entity.quran

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entity representing a Khatam Quran reading plan.
 *
 * @author hazratummar
 */
@Entity(tableName = "khatam_plan")
data class KhatamPlanEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String = "Khatam Quran",
    val startDateTimestamp: Long = System.currentTimeMillis(),
    val targetEndDateTimestamp: Long,
    val lastReadSurahNumber: Int = 1,
    val lastReadAyahNumber: Int = 1,
    val lastReadGlobalAyahNumber: Int = 1,
    val completedAyahsCount: Int = 0,
    val status: String = "IN_PROGRESS", // IN_PROGRESS, COMPLETED, EXPIRED, ENDED
    val completedTimestamp: Long? = null,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false,
    val updatedTimestamp: Long = System.currentTimeMillis()
)
