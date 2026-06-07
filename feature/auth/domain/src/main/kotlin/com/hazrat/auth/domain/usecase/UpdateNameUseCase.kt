package com.hazrat.auth.domain.usecase

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.UserData
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.UserDataError
import com.hazrat.utils.result.error.UserDataSuccess

class UpdateNameUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(name: String): Result<UserDataSuccess, UserDataError> {
        if (name.isBlank()) return Result.Error(UserDataError.INVALID_NAME)
        return repository.updateName(UserData(fullName = name))
    }
}