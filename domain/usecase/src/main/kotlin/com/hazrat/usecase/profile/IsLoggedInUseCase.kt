package com.hazrat.usecase.profile

import com.hazrat.datastore.TokenStorage
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to observe the current user authentication state reactively.
 * @author hazratummar
 */
class IsLoggedInUseCase(
    private val tokenStorage: TokenStorage
) {
    operator fun invoke(): Flow<Boolean> {
        return tokenStorage.isLoggedIn
    }
}