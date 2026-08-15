package com.hazrat.usecase.dua

import com.hazrat.domain.repository.DuaRepository
import com.hazrat.model.RecentReadDua
import kotlinx.coroutines.flow.Flow

/**
 * @author hazratummar
 */
class GetRecentDuasUseCase(
    private val duaRepository: DuaRepository
) {
    operator fun invoke(): Flow<List<RecentReadDua>> {
        return duaRepository.getRecentDuas()
    }
}
