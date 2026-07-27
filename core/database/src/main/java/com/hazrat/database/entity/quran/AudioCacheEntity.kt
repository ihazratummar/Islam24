package com.hazrat.database.entity.quran

import androidx.room.Entity

/**
 * Room Entity storing local audio file cache metadata for instant O(1) offline queries.
 *
 * @author hazratummar
 */
@Entity(
    tableName = "audio_cache",
    primaryKeys = ["globalAyahNumber", "edition"]
)
data class AudioCacheEntity(
    val globalAyahNumber: Int,
    val edition: String = "ar.alafasy",
    val localPath: String,
    val fileSize: Long,
    val timestamp: Long = System.currentTimeMillis()
)
