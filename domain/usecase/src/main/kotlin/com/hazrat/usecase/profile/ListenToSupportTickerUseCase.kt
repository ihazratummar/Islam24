package com.hazrat.usecase.profile

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.profile.SupporterTickerModel
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 08/08/26
 */

class ListenToSupportTickerUseCase(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<SupporterTickerModel> {
        return profileRepository.listenToSupporterUpdate()
    }
}