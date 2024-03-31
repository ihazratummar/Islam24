package com.hazrat.islam24.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.data.entity.NameEntity


@Dao
interface NameDao {

    /**
     * Inserts or updates a list of NameEntity objects into the database.
     * If a name with the same primary key already exists, it will be replaced.
     *
     * @param name The list of NameEntity objects to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertName(name: List<NameEntity>)

    /**
     * Retrieves all names from the database.
     *
     * @return A list of NameEntity objects representing all the names stored in the database.
     */
    @Query("SELECT * FROM  names")
    suspend fun getAllNames(): List<NameEntity>
}