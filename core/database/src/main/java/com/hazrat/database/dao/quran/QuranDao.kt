package com.hazrat.database.dao.quran

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.quran.AudioCacheEntity
import com.hazrat.database.entity.quran.AyahWithBookmarkEntity
import com.hazrat.database.entity.quran.QuranBookmarkEntity
import com.hazrat.database.entity.quran.RecentSurahEntity
import com.hazrat.database.entity.quran.SurahEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Quran data operations across:
 * 1. Surah, Ayah & Audio Cache
 * 2. Recent Reads
 * 3. Quran Bookmarks
 *
 * @author hazratummar
 * Created on 27/01/26
 */
@Dao
interface QuranDao {

    // =========================================================================
    // 1. SURAH, AYAH & AUDIO CACHE
    // =========================================================================

    @Query("SELECT * FROM surah ORDER BY surahNumber ASC")
    fun getAllSurah(): Flow<List<SurahEntity>>

    @Query("SELECT * FROM surah WHERE surahNumber = :surahNumber")
    fun getSurahByNumber(surahNumber: Int): Flow<SurahEntity>

    @Query("""
        SELECT 
            a.id,
            a.surahNumber,
            a.ayahNumber,
            a.globalAyahNumber,
            a.arabicText,
            a.englishTranslation,
            a.transliteration,
            a.tajweedText,
            CASE WHEN b.id IS NOT NULL AND b.isDeleted = 0 THEN 1 ELSE 0 END AS isBookmarked
        FROM ayah AS a
        LEFT JOIN quran_bookmark AS b
            ON a.globalAyahNumber = b.globalAyahNumber AND b.isDeleted = 0
        WHERE a.surahNumber = :surahNumber
        ORDER BY a.ayahNumber ASC
    """)
    fun getAllAyah(surahNumber: Int): Flow<List<AyahWithBookmarkEntity>>

    @Query("SELECT globalAyahNumber FROM ayah WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber LIMIT 1")
    suspend fun getGlobalAyahNumber(surahNumber: Int, ayahNumber: Int): Int?

    @Query("SELECT * FROM audio_cache WHERE globalAyahNumber = :globalAyahNumber AND edition = :edition")
    suspend fun getAudioCache(globalAyahNumber: Int, edition: String = "ar.alafasy"): AudioCacheEntity?

    @Query("SELECT * FROM audio_cache WHERE globalAyahNumber IN (:globalAyahNumbers) AND edition = :edition")
    suspend fun getAudioCacheBatch(globalAyahNumbers: List<Int>, edition: String = "ar.alafasy"): List<AudioCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioCache(audioCache: AudioCacheEntity)


    // =========================================================================
    // 2. RECENT READS (Reading History & Recents)
    // =========================================================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSurah(recent: RecentSurahEntity)

    @Query("SELECT * FROM recent_surah WHERE isDeleted = 0 ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSurahs(): Flow<List<RecentSurahEntity>>

    @Query("SELECT * FROM recent_surah WHERE isSynced = 0")
    suspend fun getUnSyncedRecentSurah(): List<RecentSurahEntity>

    @Query("SELECT * FROM recent_surah WHERE surahNumber = :surahNumber LIMIT 1")
    suspend fun getRecentSurahByNumber(surahNumber: Int): RecentSurahEntity?

    @Query("UPDATE recent_surah SET isSynced = 1 WHERE surahNumber IN (:surahNumbers)")
    suspend fun markRecentAsSynced(surahNumbers: List<Int>)

    @Query("UPDATE recent_surah SET isDeleted = 1, isSynced = 0, timestamp = :timestamp WHERE surahNumber = :surahNumber")
    suspend fun softDeleteRecentSurah(surahNumber: Int, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM recent_surah WHERE surahNumber IN (:surahNumbers)")
    suspend fun deleteRecentSurah(surahNumbers: List<Int>)


    // =========================================================================
    // 3. QURAN BOOKMARKS (Ayah Bookmarks & Sync)
    // =========================================================================

    @Query("SELECT COUNT(*) FROM quran_bookmark WHERE isDeleted = 0")
    fun getTotalBookmarkedAyahsCount(): Flow<Int>

    @Query("""
        SELECT 
            a.id,
            a.surahNumber,
            a.ayahNumber,
            a.globalAyahNumber,
            a.arabicText,
            a.englishTranslation,
            a.transliteration,
            a.tajweedText,
            1 AS isBookmarked
        FROM ayah AS a
        INNER JOIN quran_bookmark AS b
            ON a.globalAyahNumber = b.globalAyahNumber
        WHERE b.isDeleted = 0
        ORDER BY a.surahNumber ASC, a.ayahNumber ASC
    """)
    fun getBookmarkedAyahs(): Flow<List<AyahWithBookmarkEntity>>

    @Query("SELECT * FROM quran_bookmark WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber LIMIT 1")
    suspend fun getBookmark(surahNumber: Int, ayahNumber: Int): QuranBookmarkEntity?

    @Query("SELECT * FROM quran_bookmark WHERE isSynced = 0")
    suspend fun getUnSyncedBookmarks(): List<QuranBookmarkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookmark(bookmark: QuranBookmarkEntity)

    @Query("UPDATE quran_bookmark SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markBookmarkAsSynced(ids: List<String>)

    @Query("DELETE FROM quran_bookmark WHERE id IN (:ids)")
    suspend fun hardDeleteBookmark(ids: List<String>)

    @Query("UPDATE quran_bookmark SET isDeleted = :isDeleted, isSynced = 0 WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    suspend fun updateBookmarkState(surahNumber: Int, ayahNumber: Int, isDeleted: Boolean)
}