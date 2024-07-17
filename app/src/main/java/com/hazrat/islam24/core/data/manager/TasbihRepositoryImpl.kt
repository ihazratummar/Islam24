package com.hazrat.islam24.core.data.manager

import com.hazrat.islam24.core.data.dao.TasbihCounterDao
import com.hazrat.islam24.core.data.entity.TasbihCounterEntity
import com.hazrat.islam24.core.domain.repository.TasbihRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the TasbihRepository interface for managing tasbih counter data.
 *
 * @param dao The Data Access Object (DAO) for interacting with tasbih counter data in the local database.
 */
class TasbihRepositoryImpl(
    private val dao: TasbihCounterDao
) : TasbihRepository {

    /**
     * Inserts a tasbih counter entity into the local database.
     *
     * @param tasbihCounterEntity The TasbihCounterEntity object to be inserted.
     */
    override suspend fun insertTasbih(tasbihCounterEntity: TasbihCounterEntity) {
        dao.insert(tasbihCounterEntity)
    }

    /**
     * Retrieves the tasbih counter data from the local database.
     *
     * @return Flow representing the list of tasbih counter entities.
     */
    override fun getTasbih(): Flow<List<TasbihCounterEntity?>> {
        return dao.getTasbih()
    }

    /**
     * Resets the tasbih count to zero in the local database.
     */
    override suspend fun resetTasbihCount() {
        dao.resetTasbihCount()
    }
}