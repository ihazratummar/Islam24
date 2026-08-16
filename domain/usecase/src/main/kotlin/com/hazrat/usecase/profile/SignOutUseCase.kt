package com.hazrat.usecase.profile

import com.hazrat.datastore.SyncDatastore
import com.hazrat.domain.repository.AuthRepository
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.domain.repository.SyncRepository
import com.hazrat.domain.repository.prayer.PrayerLogRepository
import com.hazrat.domain.repository.prayer.PrayerSettingRepository
import com.hazrat.domain.repository.quran.KhatamRepository
import com.hazrat.domain.repository.quran.QuranRepository
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
    private val profileRepository: ProfileRepository,
    private val syncRepository: SyncRepository,
    private val quranRepository: QuranRepository,
    private val khatamRepository: KhatamRepository,
    private val prayerLogRepository: PrayerLogRepository,
    private val prayerSettingRepository: PrayerSettingRepository,
    private val syncDatastore: SyncDatastore
) {
    suspend operator fun invoke(): Result<Unit, AuthError> {
        // 1. Attempt remote logout and clear credential manager / RevenueCat state

        val syncSuccess = try {
            syncRepository.performFullSync()
        }catch (e: Exception){
            Timber.tag("SignOutUseCase").e("Pre-logout cloud sync failed")
            false
        }

        if (!syncSuccess){
            Timber.tag("SignOutUseCase").e("Sign out aborted: Cloud backup failed")
            return Result.Error(AuthError.NO_INTERNET)
        }

        // 2. Mandatory backend logout (revokes token on server)
        val logoutResult = authRepository.logout()
        if (logoutResult is Result.Error) {
            Timber.tag("SignOutUseCase").e("Sign out aborted: Backend logout failed")
            return logoutResult
        }

        // 3. Clear local Room database profile and supporter status entities
        try {
            syncDatastore.clearLastSyncedAt()
            profileRepository.deleteLocalProfile()
            profileRepository.clearLocalStatus()
            quranRepository.cleanLocalQuranData()
            khatamRepository.clearLocalQuranKhatam()
            prayerLogRepository.clearData()
            prayerSettingRepository.clearUserSetting()
        } catch (e: Exception) {
            Timber.tag("SignOutUseCase").e(e, "Failed to clean local profile data")
        }

        // 3. Always clear local session tokens last (emits false on isLoggedIn Flow)
        authRepository.clearLocalSession()

        return logoutResult
    }
}