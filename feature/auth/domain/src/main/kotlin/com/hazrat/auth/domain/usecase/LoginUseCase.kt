package com.hazrat.auth.domain.usecase

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError

/**
 * @author Hazrat Ummar Shaikh
 */

class LoginUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit, AuthError> {
        if (email.isBlank() || password.isBlank()) {
            return Result.Error(AuthError.INVALID_CREDENTIALS)
        }
        return repository.login(email, password)
    }
}