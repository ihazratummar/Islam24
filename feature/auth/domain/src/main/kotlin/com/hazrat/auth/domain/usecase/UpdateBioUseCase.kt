package com.hazrat.auth.domain.usecase

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.UserData
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.UserDataError
import com.hazrat.utils.result.error.UserDataSuccess

class UpdateBioUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(bio: String): Result<UserDataSuccess, UserDataError> {
        if (bio.isBlank()) return Result.Error(UserDataError.INVALID_BIO)
        return repository.updateBio(UserData(bio = bio))
    }
}