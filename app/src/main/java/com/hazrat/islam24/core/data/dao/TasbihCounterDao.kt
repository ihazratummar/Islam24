package com.hazrat.islam24.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.data.entity.TasbihCounterEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TasbihCounterDao {

    /**
     * Inserts or updates the TasbihCounterEntity in the database.
     * If the entity already exists, it will be replaced.
     *
     * @param tasbihCounterEntity The TasbihCounterEntity to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tasbihCounterEntity: TasbihCounterEntity)

    /**
     * Retrieves the tasbih counter data from the database.
     *
     * @return Flow representing the list of tasbih counter data as TasbihCounterEntity objects.
     */
    @Query("SELECT * FROM tasbih_counter WHERE id = 1")
    fun getTasbih(): Flow<List<TasbihCounterEntity>>

    /**
     * Resets the tasbih count to zero in the database.
     * This method updates the tasbih counter entity with ID 1.
     */
    @Query("UPDATE tasbih_counter SET tasbihCount = 0 WHERE id = 1")
    suspend fun resetTasbihCount()
}
