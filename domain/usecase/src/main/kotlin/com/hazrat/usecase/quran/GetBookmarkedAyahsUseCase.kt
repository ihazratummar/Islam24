package com.hazrat.usecase.quran

import com.hazrat.domain.repository.quran.QuranRepository
import com.hazrat.model.quran.AyahModel
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to retrieve all bookmarked Quran Ayahs.
 *
 * @author hazratummar
 */
class GetBookmarkedAyahsUseCase(
    private val quranRepository: QuranRepository
) {
    operator fun invoke(): Flow<List<AyahModel>> {
        return quranRepository.getBookmarkedAyahs()
    }
}
