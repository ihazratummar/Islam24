package com.hazrat.islam24.data.manager

import com.hazrat.islam24.data.dao.TasbihCounterDao
import com.hazrat.islam24.data.entity.TasbihCounterEntity
import com.hazrat.islam24.domain.repository.TasbihRepository
import kotlinx.coroutines.flow.Flow

class TasbihRepositoryImpl(
    private val dao: TasbihCounterDao
): TasbihRepository {
    override suspend fun insertTasbih(tasbihCounterEntity: TasbihCounterEntity) {
        dao.insert(tasbihCounterEntity)
    }

    override fun getTasbih(): Flow<List<TasbihCounterEntity?>> {
       return dao.getTasbih()
    }

    override suspend fun resetTasbihCount() {
        dao.resetTasbihCount()
    }


}