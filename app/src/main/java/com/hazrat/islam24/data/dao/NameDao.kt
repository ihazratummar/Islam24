package com.hazrat.islam24.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.data.entity.NameEntity


@Dao
interface NameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertName(name: List<NameEntity>)


    @Query("SELECT * FROM  names")
    suspend fun getAllNames(): List<NameEntity>
}