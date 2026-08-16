package com.hazrat.usecase.quran

import com.hazrat.domain.repository.quran.QuranRepository

/**
 * UseCase for toggling bookmark status of a specific Ayah in Room database.
 *
 * @author hazratummar
 */
class ToggleAyahBookmarkUseCase(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(surahNumber: Int, ayahNumber: Int, isBookmarked: Boolean) {
        quranRepository.toggleBookmark(surahNumber, ayahNumber, isBookmarked)
    }
}
