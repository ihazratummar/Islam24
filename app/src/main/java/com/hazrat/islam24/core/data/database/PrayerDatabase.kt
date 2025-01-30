package com.hazrat.islam24.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.core.data.dao.PrayerSettingDao
import com.hazrat.islam24.core.data.dao.PrayerTimeDao
import com.hazrat.islam24.core.data.entity.PrayerCalculationEntity
import com.hazrat.islam24.core.data.entity.PrayerJuristicEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity

@Database(
    entities = [PrayerTimeEntity::class,
        PrayerCalculationEntity::class, PrayerJuristicEntity::class],
    version = 22,
    exportSchema = false
)
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