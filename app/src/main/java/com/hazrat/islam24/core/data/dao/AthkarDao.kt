package com.hazrat.islam24.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.core.data.entity.AthkarDataEntity

/**
 * @author Hazrat Ummar Shaikh
 */
@Dao
interface AthkarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthkar(athkar: List<AthkarDataEntity>)

    @Query("SELECT * FROM athkar_data")
    suspend fun getAllAthkar(): List<AthkarDataEntity>

}