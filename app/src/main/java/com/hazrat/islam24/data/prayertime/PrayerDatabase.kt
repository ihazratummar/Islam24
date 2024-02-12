package com.hazrat.islam24.data.prayertime

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PrayerTimeEntity::class,PrayerSettingEntity::class], version = 1)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun prayerTimeDao(): PrayerTimeDao

    abstract fun prayerSetting(): PrayerSettingDao
}