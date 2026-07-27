package com.hazrat.usecase.quran

import com.hazrat.domain.repository.QuranRepository
import com.hazrat.model.al_quran_model.SurahModel
import kotlinx.coroutines.flow.Flow

class GetAllSurahListUseCase(
    private val quranRepository: QuranRepository
) {
    operator fun invoke(): Flow<List<SurahModel>> = quranRepository.getAllSurahList()
}
