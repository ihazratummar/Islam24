package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.core.domain.model.al_quran_model.FavoritesList
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.ArAyah
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.LocalQuranModelArItem
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_en.LocalQuranDataEnItem
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_json_bn.LocalQuranDataItemBn
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_transliteration.LocalQuranTransliterationItem
import com.hazrat.islam24.core.presentation.al_quran.QuranState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-12-2024
 */

interface QuranRepository {

    val quranState: StateFlow<QuranState>

    suspend fun downloadQuranFile()

    fun readQuranArFile() : Flow<List<LocalQuranModelArItem>>?

    fun readQuranTransliterationFile() : Flow<List<LocalQuranTransliterationItem>>?

    fun readQuranEnFile() : Flow<List<LocalQuranDataEnItem>>?

    fun readQuranBnFile() : Flow<List<LocalQuranDataItemBn>>?

    suspend fun getAllQuranData()

    fun saveLastRead(surahNumber: Int, ayahNumber: Int)

    fun toggleFavorite(quranAr : LocalQuranModelArItem, arAyah :  ArAyah)

    fun readFavorite() : Flow<FavoritesList>?

    fun refreshLastRead()

    fun loadFavoritesFromFile()

    suspend fun syncQuranDataIfLoggedIn()
    suspend fun syncQuranDataOnLogin()

    fun onAyahDropDownClick()
    fun onQuranSettingClick()


}