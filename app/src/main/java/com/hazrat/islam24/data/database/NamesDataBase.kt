package com.hazrat.islam24.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.data.dao.NameDao
import com.hazrat.islam24.data.entity.NameEntity


@Database(entities = [NameEntity::class], version = 2, exportSchema = false)
abstract class NamesDataBase : RoomDatabase() {

    /**
     * Retrieves the Data Access Object (DAO) for accessing names data.
     *
     * @return The NameDao instance.
     */
    abstract fun nameDao(): NameDao
}