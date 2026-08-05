package com.hazrat.usecase.profile

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.profile.SupporterStatusModel
import kotlinx.coroutines.flow.Flow

/**
 * Clean Architecture UseCase to observe user support status from local cache.
 * @author hazratummar
 */
class GetSupporterStatusUseCase(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<SupporterStatusModel?> {
        return profileRepository.getSupporterStatus()
    }
}
