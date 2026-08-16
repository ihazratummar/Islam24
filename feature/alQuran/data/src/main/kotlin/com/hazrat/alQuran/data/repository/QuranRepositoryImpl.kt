package com.hazrat.alQuran.data.repository

import com.hazrat.alQuran.data.mapper.toAyahModelList
import com.hazrat.alQuran.data.mapper.toModelList
import com.hazrat.database.dao.quran.QuranDao
import com.hazrat.database.entity.quran.QuranBookmarkEntity
import com.hazrat.database.entity.quran.RecentSurahEntity
import com.hazrat.domain.repository.quran.QuranRepository
import com.hazrat.model.quran.AyahModel
import com.hazrat.model.quran.RecentReadSurah
import com.hazrat.model.quran.SurahModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.util.UUID

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-12-2024
 */

class QuranRepositoryImpl(
    private val quranDao: QuranDao
) : QuranRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllSurahList(): Flow<List<SurahModel>> {
        return quranDao.getAllSurah()
            .mapLatest { it.toModelList() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getASurahAyas(surahNumber: Int): Flow<List<AyahModel>> {
        return quranDao.getAllAyah(surahNumber = surahNumber)
            .mapLatest { it.toAyahModelList() }
    }

    override suspend fun insertRecentSurah(
        surahNumber: Int,
        surahName: String,
        ayahNumber: Int,
        formattedDate: String
    ) {
        quranDao.insertRecentSurah(
            RecentSurahEntity(
                surahNumber = surahNumber,
                surahName = surahName,
                ayahNumber = ayahNumber,
                formattedDate = formattedDate,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getRecentSurahs(): Flow<List<RecentReadSurah>> {
        return quranDao.getRecentSurahs().mapLatest { list ->
            list.map { entity ->
                RecentReadSurah(
                    surahNumber = entity.surahNumber,
                    surahName = entity.surahName,
                    ayahNumber = entity.ayahNumber,
                    formattedDate = entity.formattedDate,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    override suspend fun deleteRecentSurah(surahNumber: Int) {
        quranDao.softDeleteRecentSurah(surahNumber = surahNumber)
    }

    override fun observeTotalBookmarkedAyahs(): Flow<Int> {
        return quranDao.getTotalBookmarkedAyahsCount()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBookmarkedAyahs(): Flow<List<AyahModel>> {
        return quranDao.getBookmarkedAyahs()
            .mapLatest { it.toAyahModelList() }
    }

    override suspend fun toggleBookmark(surahNumber: Int, ayahNumber: Int, isBookmarked: Boolean) {
        val existing = quranDao.getBookmark(surahNumber = surahNumber, ayahNumber = ayahNumber)
        if (existing != null) {
            quranDao.upsertBookmark(
                bookmark = existing.copy(
                    isDeleted = !isBookmarked,
                    isSynced = false,
                    updatedAt = System.currentTimeMillis()
                )
            )
        } else if (isBookmarked) {
            val globalAyah = quranDao.getGlobalAyahNumber(surahNumber = surahNumber, ayahNumber = ayahNumber) ?: 0
            quranDao.upsertBookmark(
                QuranBookmarkEntity(
                    id = UUID.randomUUID().toString(),
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    globalAyahNumber = globalAyah,
                    isDeleted = false,
                    isSynced = false,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}