package com.hazrat.usecase.quran

import com.hazrat.domain.repository.QuranRepository

class SaveRecentSurahUseCase(
    private val quranRepository: QuranRepository
) {
    suspend operator fun invoke(surahNumber: Int, surahName: String, ayahNumber: Int, formattedDate: String) {
        quranRepository.insertRecentSurah(
            surahNumber = surahNumber,
            surahName = surahName,
            ayahNumber = ayahNumber,
            formattedDate = formattedDate
        )
    }
}
