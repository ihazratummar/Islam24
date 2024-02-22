package com.hazrat.islam24.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.data.entity.LocationDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationNameDao {
    @Query("SELECT * FROM location_details WHERE id = 1")
    fun getLocationDetails(): Flow<List<LocationDetailsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocationDetails(location: LocationDetailsEntity)

    @Query("DELETE FROM location_details WHERE id != 1")
    suspend fun deleteOtherLocationDetails()
}