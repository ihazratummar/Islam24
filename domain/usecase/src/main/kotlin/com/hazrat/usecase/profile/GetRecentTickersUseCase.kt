package com.hazrat.usecase.profile

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.profile.SupporterTickerModel
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to fetch the 10 most recent cached community supporter tickers.
 * @author Hazrat Ummar Shaikh
 */
class GetRecentTickersUseCase(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<List<SupporterTickerModel>> {
        return profileRepository.getRecentTickers()
    }
}
