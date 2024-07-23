package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.core.data.entity.TasbihCounterEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing Tasbih (dhikr counter) operations.
 * This interface defines methods for inserting, retrieving, and resetting Tasbih data.
 */
interface TasbihRepository {

    /**
     * Inserts a Tasbih counter entity into the database.
     * This function saves the provided TasbihCounterEntity object.
     *
     * @param tasbihCounterEntity The TasbihCounterEntity object to be inserted into the database.
     */
    suspend fun insertTasbih(tasbihCounterEntity: TasbihCounterEntity)

    /**
     * Retrieves the list of Tasbih counter entities from the database.
     * This function returns a Flow that emits a list of TasbihCounterEntity objects.
     *
     * @return A Flow that emits a list of TasbihCounterEntity objects representing the Tasbih counters.
     */
    fun getTasbih(): Flow<List<TasbihCounterEntity?>>

    /**
     * Resets the Tasbih counter count.
     * This function clears or resets the count of Tasbih counters in the database.
     */
    suspend fun resetTasbihCount()
}