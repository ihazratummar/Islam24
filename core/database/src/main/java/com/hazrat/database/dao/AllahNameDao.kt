package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.AllahNameEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AllahNameDao {

    /**
     * Inserts or updates a list of NameEntity objects into the database.
     * If a name with the same primary key already exists, it will be replaced.
     *
     * @param name The list of NameEntity objects to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertName(name: List<AllahNameEntity>)

    /**
     * Retrieves all names from the database.
     *
     * @return A list of NameEntity objects representing all the names stored in the database.
     */
    @Query("SELECT * FROM  allah_names")
    fun getAllNames(): List<AllahNameEntity>
}