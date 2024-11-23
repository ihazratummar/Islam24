package com.hazrat.islam24.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.core.data.dao.ZakatDao
import com.hazrat.islam24.core.domain.model.zakat.NisabEntity
import com.hazrat.islam24.core.domain.model.zakat.ZakatEntity

/**
 * @author Hazrat Ummar Shaikh
 */

@Database(entities = [NisabEntity::class, ZakatEntity::class], version = 8, exportSchema = false)
abstract class ZakatDatabase : RoomDatabase(){
    abstract fun zakatDao(): ZakatDao
}