package com.hazrat.model.quran

enum class KhatamStatus {
    IN_PROGRESS,
    COMPLETED,
    EXPIRED,
    ENDED
}

data class KhatamPlanModel(
    val id: String,
    val title: String = "Khatam Quran",
    val startDateTimestamp: Long,
    val targetEndDateTimestamp: Long,
    val lastReadSurahNumber: Int = 1,
    val lastReadAyahNumber: Int = 1,
    val lastReadGlobalAyahNumber: Int = 1,
    val completedAyahsCount: Int = 0,
    val status: KhatamStatus = KhatamStatus.IN_PROGRESS,
    val completedTimestamp: Long? = null,
    val updatedTimestamp: Long = System.currentTimeMillis()
) {
    val totalAyahsCount: Int = 6236

    val progressPercentage: Float
        get() = if (totalAyahsCount == 0) 0f else ((completedAyahsCount.toFloat() / totalAyahsCount.toFloat()) * 100f).coerceIn(0f, 100f)

    val isExpired: Boolean
        get() = status == KhatamStatus.IN_PROGRESS && System.currentTimeMillis() > targetEndDateTimestamp

    val displayStatus: KhatamStatus
        get() = when {
            status == KhatamStatus.COMPLETED -> KhatamStatus.COMPLETED
            status == KhatamStatus.ENDED -> KhatamStatus.ENDED
            isExpired -> KhatamStatus.EXPIRED
            else -> KhatamStatus.IN_PROGRESS
        }
}
