package com.hazrat.usecase.khatam

import com.hazrat.domain.repository.quran.KhatamRepository

class ResetKhatamPlanUseCase(
    private val repository: KhatamRepository
) {
    suspend operator fun invoke(planId: String) {
        repository.resetKhatamPlan(planId)
    }
}
