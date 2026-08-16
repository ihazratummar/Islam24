package com.hazrat.usecase.khatam

import com.hazrat.domain.repository.quran.KhatamRepository

class StartKhatamPlanUseCase(
    private val repository: KhatamRepository
) {
    suspend operator fun invoke(targetEndDateTimestamp: Long) {
        repository.startKhatamPlan(targetEndDateTimestamp)
    }
}
