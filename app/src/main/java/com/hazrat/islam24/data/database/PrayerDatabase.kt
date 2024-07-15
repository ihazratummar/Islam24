package com.hazrat.islam24.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.data.dao.PrayerSettingDao
import com.hazrat.islam24.data.entity.PrayerSettingEntity
import com.hazrat.islam24.data.dao.PrayerTimeDao
import com.hazrat.islam24.data.entity.PrayerTimeEntity

@Database(entities = [PrayerTimeEntity::class, PrayerSettingEntity::class], version = 5, exportSchema = false)
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