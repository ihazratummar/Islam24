package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.core.data.entity.TasbihCounterEntity
import kotlinx.coroutines.flow.Flow

interface TasbihRepository {

    suspend fun insertTasbih(tasbihCounterEntity: TasbihCounterEntity)

    fun getTasbih(): Flow<List<TasbihCounterEntity?>>

    suspend fun resetTasbihCount()
}