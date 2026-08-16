package com.hazrat.domain.repository.quran

import com.hazrat.model.quran.AyahModel
import com.hazrat.model.quran.RecentReadSurah
import com.hazrat.model.quran.SurahModel
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-12-2024
 */

interface QuranRepository {

    fun getAllSurahList(): Flow<List<SurahModel>>

    fun getASurahAyas(surahNumber: Int): Flow<List<AyahModel>>

    suspend fun insertRecentSurah(surahNumber: Int, surahName: String, ayahNumber: Int, formattedDate: String)

    fun getRecentSurahs(): Flow<List<RecentReadSurah>>

    suspend fun deleteRecentSurah(surahNumber: Int)

    fun observeTotalBookmarkedAyahs(): Flow<Int>

    fun getBookmarkedAyahs(): Flow<List<AyahModel>>

    suspend fun toggleBookmark(surahNumber: Int, ayahNumber: Int, isBookmarked: Boolean)

    suspend fun cleanLocalQuranData()
}