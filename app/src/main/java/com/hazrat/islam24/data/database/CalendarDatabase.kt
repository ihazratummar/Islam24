package com.hazrat.islam24.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.hijricaneldar.data.dao.GregorianToHijriDao
import com.hazrat.hijricaneldar.data.dao.HijriCalendarDao
import com.hazrat.islam24.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.data.entity.HijriCalendarEntity

@Database(entities = [GregorianToHijriEntity::class, HijriCalendarEntity::class], version = 1)
abstract class CalendarDatabase: RoomDatabase() {
    abstract fun gregorianToHijriDao(): GregorianToHijriDao

    abstract fun hijriCalendarDao() : HijriCalendarDao
}