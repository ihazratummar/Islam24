package com.hazrat.usecase.quran

import com.hazrat.domain.repository.QuranRepository
import com.hazrat.model.al_quran_model.AyahModel
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
