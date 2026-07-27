package com.hazrat.tasbih.domain.repository

import com.hazrat.tasbih.domain.model.Tasbih
import kotlinx.coroutines.flow.Flow

interface TasbihRepository {
    fun getAllTasbihs(): Flow<List<Tasbih>>
    fun getTasbihById(id: Int): Flow<Tasbih?>
    fun getTodayTotalDhikr(dateString: String): Flow<Int>
    suspend fun incrementCount(id: Int, currentCount: Int, lifetimeCount: Int)
    suspend fun undoCount(id: Int, currentCount: Int, lifetimeCount: Int)
    suspend fun resetCount(id: Int)
    suspend fun toggleFavorite(id: Int, isFavorite: Boolean)
    suspend fun addCustomTasbih(tasbih: Tasbih): Long
    suspend fun deleteCustomTasbih(id: Int)
}
