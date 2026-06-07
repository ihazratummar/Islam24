package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.database.dao.DuaDao
import com.hazrat.database.dao.QuranDao
import com.hazrat.database.entity.DuaCategoryEntity
import com.hazrat.database.entity.DuaItemEntity
import com.hazrat.database.entity.quran.AyahEntity
import com.hazrat.database.entity.quran.SurahEntity


/**
 * @author hazratummar
 * Created on 27/01/26
 */

@Database(entities = [DuaCategoryEntity::class, DuaItemEntity::class ], version = 1, exportSchema = false)
abstract class DuaDatabase : RoomDatabase() {

    abstract fun duaDao () : DuaDao
}