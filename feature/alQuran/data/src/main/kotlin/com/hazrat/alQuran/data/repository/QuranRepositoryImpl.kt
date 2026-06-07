package com.hazrat.alQuran.data.repository

import com.hazrat.alQuran.data.mapper.toAyahModelList
import com.hazrat.alQuran.data.mapper.toModelList
import com.hazrat.database.dao.QuranDao
import com.hazrat.domain.repository.QuranRepository
import com.hazrat.model.al_quran_model.AyahModel
import com.hazrat.model.al_quran_model.SurahModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

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
}