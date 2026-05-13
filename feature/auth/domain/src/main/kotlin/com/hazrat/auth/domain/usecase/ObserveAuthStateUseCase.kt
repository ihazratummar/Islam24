package com.hazrat.auth.domain.usecase

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.AuthState
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

class ObserveAuthStateUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<AuthState> {
        return repository.authState
    }
}