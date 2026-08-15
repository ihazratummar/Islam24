package com.hazrat.usecase.dua

import com.hazrat.domain.repository.DuaRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError

/**
 * @author hazratummar
 */
class ToggleDuaBookmarkUseCase(
    private val duaRepository: DuaRepository
) {
    suspend operator fun invoke(duaId: Int, isBookmarked: Boolean): Result<Unit, DatabaseError> {
        return duaRepository.toggleBookmark(duaId, isBookmarked)
    }
}
