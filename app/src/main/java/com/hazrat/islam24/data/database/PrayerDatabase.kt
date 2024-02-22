package com.hazrat.islam24.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.data.dao.PrayerSettingDao
import com.hazrat.islam24.data.entity.PrayerSettingEntity
import com.hazrat.islam24.data.dao.PrayerTimeDao
import com.hazrat.islam24.data.entity.PrayerTimeEntity

@Database(entities = [PrayerTimeEntity::class, PrayerSettingEntity::class], version = 2)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun prayerTimeDao(): PrayerTimeDao

    abstract fun prayerSetting(): PrayerSettingDao
}