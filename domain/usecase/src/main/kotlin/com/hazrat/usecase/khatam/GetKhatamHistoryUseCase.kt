package com.hazrat.usecase.khatam

import com.hazrat.domain.repository.quran.KhatamRepository
import com.hazrat.model.quran.KhatamPlanModel
import kotlinx.coroutines.flow.Flow

class GetKhatamHistoryUseCase(
    private val repository: KhatamRepository
) {
    operator fun invoke(): Flow<List<KhatamPlanModel>> = repository.getKhatamPlanHistory()
}
