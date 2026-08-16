package com.hazrat.usecase.quran

import com.hazrat.domain.repository.quran.QuranRepository
import com.hazrat.model.quran.SurahModel
import kotlinx.coroutines.flow.Flow

class GetAllSurahListUseCase(
    private val quranRepository: QuranRepository
) {
    operator fun invoke(): Flow<List<SurahModel>> = quranRepository.getAllSurahList()
}
