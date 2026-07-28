package com.hazrat.usecase.khatam

import com.hazrat.domain.repository.KhatamRepository

class UpdateKhatamProgressUseCase(
    private val repository: KhatamRepository
) {
    suspend operator fun invoke(surahNumber: Int, ayahNumber: Int, globalAyahNumber: Int) {
        repository.updateKhatamProgress(surahNumber, ayahNumber, globalAyahNumber)
    }
}
