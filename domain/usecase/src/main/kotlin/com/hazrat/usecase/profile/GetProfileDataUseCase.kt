package com.hazrat.usecase.profile

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.profile.UserModel
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 07/08/26
 */

class GetProfileDataUseCase (
    private val profileRepository: ProfileRepository
) {

    operator fun invoke(): Flow<UserModel?> {
        return profileRepository.getProfile()
    }

}