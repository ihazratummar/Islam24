package com.hazrat.usecase.quran

import com.hazrat.domain.repository.QuranRepository
import com.hazrat.model.al_quran_model.AyahModel
import kotlinx.coroutines.flow.Flow

class GetSurahAyahsUseCase(
    private val quranRepository: QuranRepository
) {
    operator fun invoke(surahNumber: Int): Flow<List<AyahModel>> = quranRepository.getASurahAyas(surahNumber)
}
