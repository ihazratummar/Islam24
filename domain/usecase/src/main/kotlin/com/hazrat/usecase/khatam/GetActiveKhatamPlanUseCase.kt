package com.hazrat.usecase.khatam

import com.hazrat.domain.repository.KhatamRepository
import com.hazrat.model.KhatamPlanModel
import kotlinx.coroutines.flow.Flow

class GetActiveKhatamPlanUseCase(
    private val repository: KhatamRepository
) {
    operator fun invoke(): Flow<KhatamPlanModel?> = repository.getActiveKhatamPlan()
}
