package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.quran.AudioCacheEntity
import com.hazrat.database.entity.quran.AyahEntity
import com.hazrat.database.entity.quran.RecentSurahEntity
import com.hazrat.database.entity.quran.SurahEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author hazratummar
 * Created on 27/01/26
 */

@Dao
interface QuranDao {

    @Query("SELECT * FROM surah ORDER BY surahNumber ASC")
    fun getAllSurah(): Flow<List<SurahEntity>>

    @Query("SELECT * FROM surah WHERE surahNumber = :surahNumber")
    fun getSurahByNumber(surahNumber: Int): Flow<SurahEntity>

    @Query("SELECT * FROM ayah WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    fun getAllAyah(surahNumber: Int): Flow<List<AyahEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSurah(recent: RecentSurahEntity)

    @Query("SELECT * FROM recent_surah ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSurahs(): Flow<List<RecentSurahEntity>>

    @Query("DELETE FROM recent_surah WHERE surahNumber = :surahNumber")
    suspend fun deleteRecentSurah(surahNumber: Int)

    @Query("SELECT * FROM audio_cache WHERE globalAyahNumber = :globalAyahNumber AND edition = :edition")
    suspend fun getAudioCache(globalAyahNumber: Int, edition: String = "ar.alafasy"): AudioCacheEntity?

    @Query("SELECT * FROM audio_cache WHERE globalAyahNumber IN (:globalAyahNumbers) AND edition = :edition")
    suspend fun getAudioCacheBatch(globalAyahNumbers: List<Int>, edition: String = "ar.alafasy"): List<AudioCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioCache(audioCache: AudioCacheEntity)

    @Query("SELECT COUNT(*) FROM ayah WHERE isBookmarked = 1")
    fun getTotalBookmarkedAyahsCount(): Flow<Int>

    @Query("SELECT * FROM ayah WHERE isBookmarked = 1 ORDER BY surahNumber ASC, ayahNumber ASC")
    fun getBookmarkedAyahs(): Flow<List<AyahEntity>>

    @Query("UPDATE ayah SET isBookmarked = :isBookmarked WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    suspend fun updateBookmarkState(surahNumber: Int, ayahNumber: Int, isBookmarked: Boolean)
}