package com.hazrat.usecase.profile

import com.hazrat.domain.repository.AuthRepository
import com.hazrat.domain.repository.ProfileRepository
import timber.log.Timber

/**
 * UseCase to sign out the user locally and remotely.
 * Always clears local tokens even if the remote network request fails.
 * @author hazratummar
 */
class SignOutUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Boolean {
        val remoteLogOutSuccess = authRepository.logout()

        try {
            profileRepository.deleteLocalProfile()
        } catch (e: Exception) {
            Timber.tag("SignOutUseCase").e("Failed to Delete profile $e")
        }

        try {
            profileRepository.clearLocalStatus()
        } catch (e: Exception) {
            Timber.tag("SignOutUseCase").e("Failed to clear support status $e")
        }

        // Always remove local authentication state.
        try {
            authRepository.clearLocalSession()
        } catch (e: Exception) {
            Timber.tag("SignOutUseCase").e("Failed to clear tokens $e")
        }
        return remoteLogOutSuccess

    }
}