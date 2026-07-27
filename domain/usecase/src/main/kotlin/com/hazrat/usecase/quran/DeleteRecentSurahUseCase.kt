package com.hazrat.usecase.quran

import com.hazrat.domain.repository.QuranRepository

/**
 * UseCase to delete a completed surah from recent reads.
 * Called when the user scrolls to the end of a surah and lingers there,
 * indicating they have finished reading it.
 *
 * @author hazratummar
 */
class DeleteRecentSurahUseCase(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(surahNumber: Int) {
        quranRepository.deleteRecentSurah(surahNumber)
    }
}
