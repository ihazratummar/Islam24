package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hazrat.database.entity.TasbihEntity
import com.hazrat.database.entity.TasbihLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasbihDao {

    @Query("SELECT * FROM tasbih_table ORDER BY isFavorite DESC, id ASC")
    fun getAllTasbihs(): Flow<List<TasbihEntity>>

    @Query("SELECT * FROM tasbih_table WHERE id = :id")
    fun getTasbihById(id: Int): Flow<TasbihEntity?>

    @Query("SELECT SUM(countAdded) FROM tasbih_log_table WHERE dateString = :dateString")
    fun getTodayTotalCount(dateString: String): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasbih(entity: TasbihEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<TasbihEntity>)

    @Query("UPDATE tasbih_table SET currentCount = :currentCount, totalLifetimeCount = :lifetimeCount, lastUpdatedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateCount(id: Int, currentCount: Int, lifetimeCount: Int, timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: TasbihLogEntity)

    @Query("UPDATE tasbih_table SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Int, isFavorite: Boolean)

    @Query("UPDATE tasbih_table SET currentCount = 0 WHERE id = :id")
    suspend fun resetCount(id: Int)

    @Query("DELETE FROM tasbih_table WHERE id = :id AND isCustom = 1")
    suspend fun deleteCustomTasbih(id: Int)
}
