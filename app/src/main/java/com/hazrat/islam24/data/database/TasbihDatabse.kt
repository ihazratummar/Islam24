package com.hazrat.islam24.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.data.dao.TasbihCounterDao
import com.hazrat.islam24.data.entity.TasbihCounterEntity

@Database(entities = [TasbihCounterEntity::class], version = 1)
abstract class TasbihDatabase: RoomDatabase() {

    abstract fun tasbihCounterDao(): TasbihCounterDao

}