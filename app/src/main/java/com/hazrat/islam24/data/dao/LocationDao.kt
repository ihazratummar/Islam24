package com.hazrat.islam24.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.data.entity.LocationEntity


@Dao
interface LocationDao {
    @Query("SELECT * FROM location_data WHERE id = 1")
    suspend fun getLocation(): LocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(location: LocationEntity)


}