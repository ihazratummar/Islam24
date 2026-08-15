package com.hazrat.usecase.dua

import com.hazrat.domain.repository.DuaRepository

/**
 * @author hazratummar
 */
class DeleteRecentDuaUseCase(
    private val duaRepository: DuaRepository
) {
    suspend operator fun invoke(chapterId: Int) {
        duaRepository.deleteRecentDua(chapterId)
    }
}
