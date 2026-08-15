package com.hazrat.usecase.dua

import com.hazrat.domain.repository.DuaRepository

/**
 * @author hazratummar
 */
class SaveRecentDuaUseCase(
    private val duaRepository: DuaRepository
) {
    suspend operator fun invoke(chapterId: Int, title: String, duaCount: Int, formattedDate: String) {
        duaRepository.insertRecentDua(chapterId, title, duaCount, formattedDate)
    }
}
