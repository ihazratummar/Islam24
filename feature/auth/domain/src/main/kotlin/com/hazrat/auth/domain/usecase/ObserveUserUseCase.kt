package com.hazrat.auth.domain.usecase

import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.UserData
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

class ObserveUserUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<UserData?> {
        return repository.currentUser
    }
}