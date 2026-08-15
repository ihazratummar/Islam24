package com.hazrat.usecase.dua

import com.hazrat.domain.repository.DuaRepository
import com.hazrat.model.DuaChapterWithCountModel
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.flow.Flow

/**
 * @author hazratummar
 */
class GetCategoryChaptersUseCase(
    private val duaRepository: DuaRepository
) {
    operator fun invoke(category: HisnulMuslimCategory): Flow<Result<List<DuaChapterWithCountModel>, DatabaseError>> {
        return duaRepository.getChaptersForCategory(category)
    }
}
