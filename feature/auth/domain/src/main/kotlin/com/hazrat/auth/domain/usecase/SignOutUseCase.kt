package com.hazrat.auth.domain.usecase

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError

/**
 * @author Hazrat Ummar Shaikh
 */

class SignOutUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<Unit, AuthError> {
        return repository.signOut()
    }
}