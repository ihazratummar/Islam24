package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.GregorianToHijriEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GregorianToHijriDao {

    /**
     * Inserts or updates the GregorianToHijriEntity in the database.
     * If the entity already exists, it will be replaced.
     *
     * @param entity The GregorianToHijriEntity to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: GregorianToHijriEntity)

    /**
     * Retrieves the Gregorian to Hijri conversion data from the database.
     * Emits the result as a Flow of List of GregorianToHijriEntity.
     *
     * @return Flow representing the Gregorian to Hijri conversion data.
     */
    @Query("SELECT * FROM gregoriantohijrientity WHERE id = 1")
    fun getGregorianToHijriData(): Flow<List<GregorianToHijriEntity>>
}