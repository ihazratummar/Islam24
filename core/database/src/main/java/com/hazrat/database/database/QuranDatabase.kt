package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.database.dao.quran.KhatamDao
import com.hazrat.database.dao.quran.QuranDao
import com.hazrat.database.entity.quran.AudioCacheEntity
import com.hazrat.database.entity.quran.AyahEntity
import com.hazrat.database.entity.quran.KhatamPlanEntity
import com.hazrat.database.entity.quran.QuranBookmarkEntity
import com.hazrat.database.entity.quran.RecentSurahEntity
import com.hazrat.database.entity.quran.SurahEntity

/**
 * @author hazratummar
 * Created on 27/01/26
 */

@Database(
    entities = [
        SurahEntity::class,
        AyahEntity::class,
        RecentSurahEntity::class,
        AudioCacheEntity::class,
        KhatamPlanEntity::class,
        QuranBookmarkEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class QuranDatabase : RoomDatabase() {

    abstract fun quranDao(): QuranDao
    abstract fun khatamDao(): KhatamDao

}