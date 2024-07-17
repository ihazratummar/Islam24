package com.hazrat.islam24.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.core.data.dao.GregorianToHijriDao
import com.hazrat.islam24.core.data.dao.HijriCalendarDao
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.core.data.entity.HijriCalendarEntity

@Database(entities = [GregorianToHijriEntity::class, HijriCalendarEntity::class], version = 4, exportSchema = false)
abstract class CalendarDatabase: RoomDatabase() {

    /**
     * Retrieves the Data Access Object (DAO) for accessing Gregorian to Hijri conversion data.
     *
     * @return The GregorianToHijriDao instance.
     */
    abstract fun gregorianToHijriDao(): GregorianToHijriDao

    /**
     * Retrieves the Data Access Object (DAO) for accessing Hijri calendar data.
     *
     * @return The HijriCalendarDao instance.
     */
    abstract fun hijriCalendarDao() : HijriCalendarDao
}