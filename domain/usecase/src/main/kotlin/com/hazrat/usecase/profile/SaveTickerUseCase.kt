package com.hazrat.usecase.profile

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.profile.SupporterTickerModel

/**
 * Clean Architecture UseCase to save a newly generated supporter ticker into Room database.
 * @author Hazrat Ummar Shaikh
 */
class SaveTickerUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(ticker: SupporterTickerModel) {
        profileRepository.saveTicker(ticker)
    }
}
