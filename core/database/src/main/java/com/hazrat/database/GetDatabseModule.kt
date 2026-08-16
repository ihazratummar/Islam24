package com.hazrat.database

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hazrat.database.dao.AllahNameDao
import com.hazrat.database.dao.DuaDao
import com.hazrat.database.dao.LocationNameDao
import com.hazrat.database.dao.prayer.PrayerLogDao
import com.hazrat.database.dao.prayer.PrayerTimeDao
import com.hazrat.database.dao.quran.KhatamDao
import com.hazrat.database.dao.quran.QuranDao
import com.hazrat.database.dao.UserDao
import com.hazrat.database.dao.UserSupportStatusDao
import com.hazrat.database.dao.ZakatDao
import com.hazrat.database.dao.prayer.PrayerSettingDao
import com.hazrat.database.database.AppDatabase
import com.hazrat.database.database.DuaDatabase
import com.hazrat.database.database.LocationDatabase
import com.hazrat.database.database.NamesDataBase
import com.hazrat.database.database.PrayerDatabase
import com.hazrat.database.database.QuranDatabase
import com.hazrat.database.database.ZakatDatabase
import com.hazrat.database.migration.QURAN_KHATAM_MIGRATION_4_5
import com.hazrat.database.migration.QURAN_SURAH_MIGRATION_5_6
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

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `supporter_ticker` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`donorName` TEXT NOT NULL, " +
                    "`type` TEXT NOT NULL, " +
                    "`amount` REAL, " +
                    "`currency` TEXT, " +
                    "`timestamp` INTEGER NOT NULL)"
        )
    }
}

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_5_6)
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }

    single<UserSupportStatusDao> { get<AppDatabase>().userSupportStatusDao() }
    single<UserDao> { get<AppDatabase>().userDao() }
    single<com.hazrat.database.dao.SupporterTickerDao> { get<AppDatabase>().supporterTickerDao() }

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
    single<PrayerLogDao> { get<PrayerDatabase>().prayerLogDao() }
    single<PrayerSettingDao> { get<PrayerDatabase>().prayerSettingDao() }

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
    // Migration v0/v1 -> v4: add missing tables from asset version 0/1
    val MIGRATION_0_4 = object : Migration(0, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `recent_surah` (
                    `surahNumber` INTEGER NOT NULL,
                    `surahName` TEXT NOT NULL,
                    `ayahNumber` INTEGER NOT NULL,
                    `formattedDate` TEXT NOT NULL,
                    `timestamp` INTEGER NOT NULL,
                    PRIMARY KEY(`surahNumber`)
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `audio_cache` (
                    `globalAyahNumber` INTEGER NOT NULL,
                    `edition` TEXT NOT NULL,
                    `localPath` TEXT NOT NULL,
                    `fileSize` INTEGER NOT NULL,
                    `timestamp` INTEGER NOT NULL,
                    PRIMARY KEY(`globalAyahNumber`, `edition`)
                )
                """.trimIndent()
            )
        }
    }

    val MIGRATION_1_4 = object : Migration(1, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `recent_surah` (
                    `surahNumber` INTEGER NOT NULL,
                    `surahName` TEXT NOT NULL,
                    `ayahNumber` INTEGER NOT NULL,
                    `formattedDate` TEXT NOT NULL,
                    `timestamp` INTEGER NOT NULL,
                    PRIMARY KEY(`surahNumber`)
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `audio_cache` (
                    `globalAyahNumber` INTEGER NOT NULL,
                    `edition` TEXT NOT NULL,
                    `localPath` TEXT NOT NULL,
                    `fileSize` INTEGER NOT NULL,
                    `timestamp` INTEGER NOT NULL,
                    PRIMARY KEY(`globalAyahNumber`, `edition`)
                )
                """.trimIndent()
            )
        }
    }

    // Migration v2 -> v3: add recent_surah table (user read history)
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `recent_surah` (
                    `surahNumber` INTEGER NOT NULL,
                    `surahName` TEXT NOT NULL,
                    `ayahNumber` INTEGER NOT NULL,
                    `formattedDate` TEXT NOT NULL,
                    `timestamp` INTEGER NOT NULL,
                    PRIMARY KEY(`surahNumber`)
                )
                """.trimIndent()
            )
        }
    }

    // Migration v3 -> v4: add audio_cache table for offline recitation playback
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `audio_cache` (
                    `globalAyahNumber` INTEGER NOT NULL,
                    `edition` TEXT NOT NULL,
                    `localPath` TEXT NOT NULL,
                    `fileSize` INTEGER NOT NULL,
                    `timestamp` INTEGER NOT NULL,
                    PRIMARY KEY(`globalAyahNumber`, `edition`)
                )
                """.trimIndent()
            )
        }
    }





    single {
        Room.databaseBuilder(
            androidContext(),
            QuranDatabase::class.java,
            "quran_db"
        )
            .createFromAsset("databases/quran_prepopulated.db")
            .addMigrations(
                MIGRATION_0_4,
                MIGRATION_1_4,
                MIGRATION_2_3,
                MIGRATION_3_4,
                QURAN_KHATAM_MIGRATION_4_5,
                QURAN_SURAH_MIGRATION_5_6
            )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }
    single<QuranDao> { get<QuranDatabase>().quranDao() }
    single<KhatamDao> { get<QuranDatabase>().khatamDao() }

val MIGRATION_0_2_DUA = object : Migration(0, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `recent_dua` (
                `chapterId` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `duaCount` INTEGER NOT NULL,
                `formattedDate` TEXT NOT NULL,
                `timestamp` INTEGER NOT NULL,
                PRIMARY KEY(`chapterId`)
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_0_1_DUA = object : Migration(0, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // No schema changes between 0 and 1
    }
}

val MIGRATION_1_2_DUA = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `recent_dua` (
                `chapterId` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `duaCount` INTEGER NOT NULL,
                `formattedDate` TEXT NOT NULL,
                `timestamp` INTEGER NOT NULL,
                PRIMARY KEY(`chapterId`)
            )
            """.trimIndent()
        )
    }
}

    // Dua Hisnul Muslim Database
    single {
        Room.databaseBuilder(
            androidContext(),
            DuaDatabase::class.java,
            "dua_db"
        )
            .createFromAsset("databases/hisnul_muslim.db")
            .addMigrations(MIGRATION_0_1_DUA, MIGRATION_1_2_DUA, MIGRATION_0_2_DUA)
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }

    single<DuaDao> { get<DuaDatabase>().duaDao() }

    // Tasbih Database
    single {
        Room.databaseBuilder(
            androidContext(),
            com.hazrat.database.database.TasbihDatabase::class.java,
            "tasbih_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }

    single<com.hazrat.database.dao.TasbihDao> { get<com.hazrat.database.database.TasbihDatabase>().tasbihDao() }
}