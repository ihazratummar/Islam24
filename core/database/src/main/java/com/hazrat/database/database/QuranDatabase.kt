package com.hazrat.database.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hazrat.database.dao.QuranDao
import com.hazrat.database.entity.quran.FavoriteAyahEntity


/**
 * @author hazratummar
 * Created on 27/01/26
 */

@Database(entities = [FavoriteAyahEntity::class], version = 1, exportSchema = false)
abstract class QuranDatabase : RoomDatabase() {

    abstract fun quranDao() : QuranDao

}