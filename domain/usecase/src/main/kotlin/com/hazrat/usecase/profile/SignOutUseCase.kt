package com.hazrat.usecase.profile

import com.hazrat.domain.repository.AuthRepository
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError
import timber.log.Timber

/**
 * Industry-Grade UseCase to orchestrate user sign out across domain repositories.
 * 1. Performs remote logout API & CredentialManager cleanup.
 * 2. Clears local Room DB user profile and supporter status.
 * 3. Clears local session tokens (triggering auth state Flow observers).
 *
 * @author hazratummar
 */
class SignOutUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<Unit, AuthError> {
        // 1. Attempt remote logout and clear credential manager / RevenueCat state
        val logoutResult = try {
            authRepository.logout()
        } catch (e: Exception) {
            Timber.tag("SignOutUseCase").e(e, "Remote logout failed")
            Result.Error(AuthError.UNKNOWN_ERROR)
        }

        // 2. Clear local Room database profile and support status entities
        try {
            profileRepository.deleteLocalProfile()
        } catch (e: Exception) {
            Timber.tag("SignOutUseCase").e(e, "Failed to delete local profile")
        }

        try {
            profileRepository.clearLocalStatus()
        } catch (e: Exception) {
            Timber.tag("SignOutUseCase").e(e, "Failed to clear support status")
        }

        // 3. Always clear local session tokens last (emits false on isLoggedIn Flow)
        try {
            authRepository.clearLocalSession()
        } catch (e: Exception) {
            Timber.tag("SignOutUseCase").e(e, "Failed to clear local tokens")
        }

        return logoutResult
    }
}