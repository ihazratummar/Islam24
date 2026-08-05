package com.hazrat.usecase.profile

import com.hazrat.domain.repository.ProfileRepository

/**
 * Clean Architecture UseCase to trigger fetching updated support status from backend API.
 * @author hazratummar
 */
class SyncSupporterStatusUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke() {
        profileRepository.insertSupporterStatus()
    }
}
