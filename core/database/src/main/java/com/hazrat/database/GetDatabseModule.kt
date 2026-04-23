package com.hazrat.database

import androidx.room.Room
import com.hazrat.database.dao.AthkarDao
import com.hazrat.database.dao.GregorianToHijriDao
import com.hazrat.database.dao.HijriCalendarDao
import com.hazrat.database.dao.LocationDao
import com.hazrat.database.dao.LocationNameDao
import com.hazrat.database.dao.AllahNameDao
import com.hazrat.database.dao.PrayerSettingDao
import com.hazrat.database.dao.PrayerTimeDao
import com.hazrat.database.dao.QuranDao
import com.hazrat.database.dao.ZakatDao
import com.hazrat.database.database.AthkarDatabase
import com.hazrat.database.database.CalendarDatabase
import com.hazrat.database.database.LocationDatabase
import com.hazrat.database.database.NamesDataBase
import com.hazrat.database.database.PrayerDatabase
import com.hazrat.database.database.QuranDatabase
import com.hazrat.database.database.ZakatDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 22/01/26
 */

fun getDatabaseModule(): Module = module {

    // Names Database
    single {
        Room.databaseBuilder(
            androidContext(),
            NamesDataBase::class.java,
            "names_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }
    single<AllahNameDao> { get<NamesDataBase>().nameDao() }

    // Athkar Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AthkarDatabase::class.java,
            "athkar_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }
    single<AthkarDao> { get<AthkarDatabase>().athkarDao() }

    // Calendar Database
    single {
        Room.databaseBuilder(
            androidContext(),
            CalendarDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }
    single<HijriCalendarDao> { get<CalendarDatabase>().hijriCalendarDao() }
    single<GregorianToHijriDao> { get<CalendarDatabase>().gregorianToHijriDao() }

    // Location Database
    single {
        Room.databaseBuilder(
            androidContext(),
            LocationDatabase::class.java,
            "location_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }
    single<LocationDao> { get<LocationDatabase>().locationDao() }
    single<LocationNameDao> { get<LocationDatabase>().locationNameDao() }

    // Prayer Database
    single {
        Room.databaseBuilder(
            androidContext(),
            PrayerDatabase::class.java,
            "prayer-database"
        )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }
    single<PrayerTimeDao> { get<PrayerDatabase>().prayerTimeDao() }
    single<PrayerSettingDao> { get<PrayerDatabase>().prayerSetting() }

    // Zakat Database
    single {
        Room.databaseBuilder(
            androidContext(),
            ZakatDatabase::class.java,
            "nisab_db"
        )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }
    single<ZakatDao> { get<ZakatDatabase>().zakatDao() }

    // Quran Database
    single {
        Room.databaseBuilder(
            androidContext(),
            QuranDatabase::class.java,
            "quran_db"
        )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }
    single<QuranDao> { get<QuranDatabase>().quranDao() }
}