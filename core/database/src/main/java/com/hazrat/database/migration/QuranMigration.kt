package com.hazrat.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * @author hazratummar
 * Created on 16/08/26
 */

// Migration v4 -> v5: add khatam_plan table
val QURAN_KHATAM_MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `khatam_plan` (
                    `id` TEXT NOT NULL,
                    `title` TEXT NOT NULL,
                    `startDateTimestamp` INTEGER NOT NULL,
                    `targetEndDateTimestamp` INTEGER NOT NULL,
                    `lastReadSurahNumber` INTEGER NOT NULL,
                    `lastReadAyahNumber` INTEGER NOT NULL,
                    `lastReadGlobalAyahNumber` INTEGER NOT NULL,
                    `completedAyahsCount` INTEGER NOT NULL,
                    `status` TEXT NOT NULL,
                    `completedTimestamp` INTEGER,
                    `updatedTimestamp` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
            """.trimIndent()
        )
    }
}

val QURAN_SURAH_MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `recent_surah` ADD COLUMN `isSynced` INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE `recent_surah` ADD COLUMN `isDeleted` INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE `khatam_plan` ADD COLUMN `isSynced` INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE `khatam_plan` ADD COLUMN `isDeleted` INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE `ayah` DROP COLUMN `isBookmarked`")
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `quran_bookmark` (
                    `id` TEXT NOT NULL,
                    `surahNumber` INTEGER NOT NULL,
                    `ayahNumber` INTEGER NOT NULL,
                    `globalAyahNumber` INTEGER NOT NULL,
                    `isDeleted` INTEGER NOT NULL DEFAULT 0,
                    `isSynced` INTEGER NOT NULL DEFAULT 0,
                    `updatedAt` INTEGER NOT NULL DEFAULT 0,
                    PRIMARY KEY(`id`),
                    FOREIGN KEY(`globalAyahNumber`) REFERENCES `ayah`(`globalAyahNumber`) ON DELETE CASCADE
                )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quran_bookmark_globalAyahNumber` ON `quran_bookmark` (`globalAyahNumber`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quran_bookmark_surahNumber_ayahNumber` ON `quran_bookmark` (`surahNumber`, `ayahNumber`)")
    }
}

val QURAN_TRANSLATIONS_MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `ayah` ADD COLUMN `bnMuhiuddin` TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE `ayah` ADD COLUMN `bnTaisirul` TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE `ayah` ADD COLUMN `bnMujibur` TEXT NOT NULL DEFAULT ''")
    }
}

val QURAN_BN_TRANSLITERATION_MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `ayah` ADD COLUMN `bnTransliteration` TEXT NOT NULL DEFAULT ''")
    }
}