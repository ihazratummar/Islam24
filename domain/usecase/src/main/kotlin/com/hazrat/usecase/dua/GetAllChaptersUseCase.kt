package com.hazrat.usecase.dua

import com.hazrat.domain.repository.DuaRepository
import com.hazrat.model.DuaChapterWithCountModel
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.flow.Flow

/**
 * @author hazratummar
 */
class GetAllChaptersUseCase(
    private val duaRepository: DuaRepository
) {
    operator fun invoke(): Flow<Result<List<DuaChapterWithCountModel>, DatabaseError>> {
        return duaRepository.getAllChapters()
    }
}
