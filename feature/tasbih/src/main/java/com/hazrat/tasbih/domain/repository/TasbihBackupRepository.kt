package com.hazrat.tasbih.domain.repository

import com.hazrat.tasbih.domain.model.Tasbih
import kotlinx.coroutines.flow.Flow

interface TasbihBackupRepository {
    suspend fun backupToCloud(userId: String): Result<Unit>
    suspend fun restoreFromCloud(userId: String): Result<List<Tasbih>>
    fun getLastBackupTimestamp(): Flow<Long?>
}
