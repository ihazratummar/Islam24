package com.hazrat.usecase.quran

import com.hazrat.domain.repository.quran.QuranRepository
import com.hazrat.model.quran.RecentReadSurah
import kotlinx.coroutines.flow.Flow

class GetRecentSurahsUseCase(
    private val quranRepository: QuranRepository
) {
    operator fun invoke(): Flow<List<RecentReadSurah>> = quranRepository.getRecentSurahs()
}
