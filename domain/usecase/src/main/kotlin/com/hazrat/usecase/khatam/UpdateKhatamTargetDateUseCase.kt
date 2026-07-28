package com.hazrat.usecase.khatam

import com.hazrat.domain.repository.KhatamRepository

class UpdateKhatamTargetDateUseCase(
    private val repository: KhatamRepository
) {
    suspend operator fun invoke(planId: String, newTargetDateTimestamp: Long) {
        repository.updateKhatamTargetDate(planId, newTargetDateTimestamp)
    }
}
