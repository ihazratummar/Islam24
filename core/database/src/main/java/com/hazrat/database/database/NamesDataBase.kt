package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.database.dao.AllahNameDao
import com.hazrat.database.entity.AllahNameEntity


@Database(entities = [AllahNameEntity::class], version = 5, exportSchema = false)
abstract class NamesDataBase : RoomDatabase() {

    /**
     * Retrieves the Data Access Object (DAO) for accessing names data.
     *
     * @return The NameDao instance.
     */
    abstract fun nameDao(): AllahNameDao
}