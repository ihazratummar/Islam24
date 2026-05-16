package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hazrat.database.converter.Converter
import com.hazrat.database.dao.PrayerSettingDao
import com.hazrat.database.dao.PrayerTimeDao
import com.hazrat.database.entity.PrayerCalculationEntity
import com.hazrat.database.entity.PrayerJuristicEntity
import com.hazrat.database.entity.PrayerTimeEntity

@Database(
    entities = [PrayerTimeEntity::class,
        PrayerCalculationEntity::class, PrayerJuristicEntity::class],
    version = 26,
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

    /**
     * Retrieves the Data Access Object (DAO) for accessing prayer setting data.
     *
     * @return The PrayerSettingDao instance.
     */
    abstract fun prayerSetting(): PrayerSettingDao
}