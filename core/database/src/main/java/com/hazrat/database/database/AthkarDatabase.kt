package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.database.dao.AthkarDao
import com.hazrat.database.entity.AthkarDataEntity

/**
 * @author Hazrat Ummar Shaikh
 */

@Database(entities = [AthkarDataEntity::class], version = 1, exportSchema = false)
abstract class AthkarDatabase : RoomDatabase(){

    abstract fun athkarDao(): AthkarDao
}