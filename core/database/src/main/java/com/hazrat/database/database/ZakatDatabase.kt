package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.database.dao.ZakatDao
import com.hazrat.database.entity.zakat.NisabEntity
import com.hazrat.database.entity.zakat.ZakatEntity

/**
 * @author Hazrat Ummar Shaikh
 */

@Database(entities = [NisabEntity::class, ZakatEntity::class], version = 12, exportSchema = false)
abstract class ZakatDatabase : RoomDatabase(){
    abstract fun zakatDao(): ZakatDao
}