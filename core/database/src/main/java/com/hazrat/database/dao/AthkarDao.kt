package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.AthkarDataEntity

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