package com.hazrat.islam24.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.core.data.dao.AthkarDao
import com.hazrat.islam24.core.data.entity.AthkarDataEntity

/**
 * @author Hazrat Ummar Shaikh
 */

@Database(entities = [AthkarDataEntity::class], version = 1, exportSchema = false)
abstract class AthkarDatabase : RoomDatabase(){

    abstract fun athkarDao(): AthkarDao
}