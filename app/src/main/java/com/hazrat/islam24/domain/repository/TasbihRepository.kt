package com.hazrat.islam24.domain.repository

import com.hazrat.islam24.data.entity.TasbihCounterEntity
import kotlinx.coroutines.flow.Flow

interface TasbihRepository {

    suspend fun insertTasbih(tasbihCounterEntity: TasbihCounterEntity)

    fun getTasbih(): Flow<List<TasbihCounterEntity?>>

    suspend fun resetTasbihCount()
}