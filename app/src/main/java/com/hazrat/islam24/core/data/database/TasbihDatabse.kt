package com.hazrat.islam24.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.core.data.dao.TasbihCounterDao
import com.hazrat.islam24.core.data.entity.TasbihCounterEntity

@Database(entities = [TasbihCounterEntity::class], version = 2, exportSchema = false)
abstract class TasbihDatabase: RoomDatabase() {

    /**
     * Retrieves the Data Access Object (DAO) for accessing tasbih counter data.
     *
     * @return The TasbihCounterDao instance.
     */
    abstract fun tasbihCounterDao(): TasbihCounterDao
}