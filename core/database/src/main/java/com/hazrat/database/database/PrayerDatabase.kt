package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hazrat.database.converter.Converter
import com.hazrat.database.dao.PrayerLogDao
import com.hazrat.database.dao.PrayerTimeDao
import com.hazrat.database.entity.PrayerLogEntity
import com.hazrat.database.entity.PrayerTimeEntity

@Database(
    entities = [PrayerTimeEntity::class, PrayerLogEntity::class],
    version = 29,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class PrayerDatabase : RoomDatabase() {

    /**
     * Retrieves the Data Access Object (DAO) for accessing prayer time data.
     *
     * @return The PrayerTimeDao instance.
     */
    abstract fun prayerTimeDao(): PrayerTimeDao

    abstract fun prayerLogDao () : PrayerLogDao
}