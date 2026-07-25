package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.database.dao.TasbihDao
import com.hazrat.database.entity.TasbihEntity
import com.hazrat.database.entity.TasbihLogEntity

@Database(
    entities = [TasbihEntity::class, TasbihLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TasbihDatabase : RoomDatabase() {
    abstract fun tasbihDao(): TasbihDao
}
