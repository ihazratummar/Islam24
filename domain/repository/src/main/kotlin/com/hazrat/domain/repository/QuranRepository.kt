package com.hazrat.domain.repository

import com.hazrat.model.al_quran_model.FavoriteAyah
import com.hazrat.model.al_quran_model.FavoritesList
import com.hazrat.model.al_quran_model.local_quran_ar.ArAyah
import com.hazrat.model.al_quran_model.local_quran_ar.LocalQuranModelArItem
import com.hazrat.model.al_quran_model.local_quran_en.LocalQuranDataEnItem
import com.hazrat.model.al_quran_model.local_quran_json_bn.LocalQuranDataItemBn
import com.hazrat.model.al_quran_model.local_quran_transliteration.LocalQuranTransliterationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-12-2024
 */

interface QuranRepository {

//    val quranState: StateFlow<QuranState>

    suspend fun downloadQuranFile()

    fun readQuranArFile(): List<LocalQuranModelArItem>?

    fun readQuranTransliterationFile(): List<LocalQuranTransliterationItem>?

    fun readQuranEnFile(): List<LocalQuranDataEnItem>?

    fun readQuranBnFile(): List<LocalQuranDataItemBn>?

    suspend fun getAllQuranData()

    fun saveLastRead(surahNumber: Int, ayahNumber: Int)

    suspend fun toggleFavorite(surahNumber: Int, ayahNumber: Int)

    fun readFavorite(): Flow<List<FavoriteAyah>>?

    suspend fun syncQuranDataIfLoggedIn()
    suspend fun syncQuranDataOnLogin()


}