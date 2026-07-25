package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}