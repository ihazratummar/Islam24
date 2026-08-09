package com.hazrat.auth.ui.appSetting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewManagerFactory
import com.hazrat.datastore.AppDataStore
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerLogRepository
import com.hazrat.domain.repository.QuranRepository
import com.hazrat.usecase.profile.GetProfileDataUseCase
import com.hazrat.usecase.profile.IsLoggedInUseCase
import com.hazrat.usecase.profile.SignOutUseCase
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * @author Hazrat Ummar Shaikh
 */
class ProfileViewModel(
    private val context: Context,
    private val appDataStore: AppDataStore,
    private val userDataStore: UserDataStore,
    private val prayerLogRepository: PrayerLogRepository? = null,
    private val quranRepository: QuranRepository? = null,
    private val logoutUseCase: SignOutUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        ProfileState()
    )
    val appSettingState: StateFlow<ProfileState> = combine(
        _state,
        appDataStore.isDarkModeEnabled,
        appDataStore.isHapticEnabled,
        userDataStore.isMasterNotificationEnabled
    ) { state, isDarkModeEnabled, isHapticEnabled, isMasterNotif ->
        state.copy(
            toggleTheme = isDarkModeEnabled,
            isHapticFeedbackEnabled = isHapticEnabled,
            isMasterNotificationEnabled = isMasterNotif
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ProfileState()
    )

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()


    init {
        viewModelScope.launch {
            val darkMode = appDataStore.getDarkModeEnabled()
            val hapticFeedback = appDataStore.getHapticEnabled()

            _state.update {
                it.copy(
                    toggleTheme = darkMode,
                    isHapticFeedbackEnabled = hapticFeedback,
                )
            }
        }

        // Real Database Metrics Flow Subscriptions
        prayerLogRepository?.let { repo ->
            viewModelScope.launch {
                repo.observeTotalLoggedPrayers().collectLatest { count ->
                    _state.update { it.copy(totalPrayersLogged = count) }
                }
            }
            viewModelScope.launch {
                repo.observePrayerStreak().collectLatest { streak ->
                    _state.update { it.copy(prayerStreak = streak) }
                }
            }
        }

        quranRepository?.let { repo ->
            viewModelScope.launch {
                repo.observeTotalBookmarkedAyahs().collectLatest { count ->
                    _state.update { it.copy(totalBookmarkedAyahs = count) }
                }
            }
        }

        loadUser()
        loginState()
    }

    private fun loginState(){
        viewModelScope.launch {
            isLoggedInUseCase().collectLatest { login->
                _state.update { it.copy(isLoggedIn = login) }
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            getProfileDataUseCase().collectLatest { userModel ->
                _state.update {
                    it.copy(
                        userModel = userModel
                    )
                }
            }
        }
    }

    fun onAppSettingEvent(event: AppSettingEvent) {
        when (event) {
            AppSettingEvent.OpenAppSetting -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }

            AppSettingEvent.HapticFeedbackClick -> {
                _state.update { it.copy(isHapticFeedbackEnabled = !it.isHapticFeedbackEnabled) }
                viewModelScope.launch {
                    appDataStore.enableHaptic(_state.value.isHapticFeedbackEnabled)
                }
            }

            AppSettingEvent.ToggleTheme -> {
                val newTheme = !_state.value.toggleTheme
                _state.update { it.copy(toggleTheme = newTheme) }
                viewModelScope.launch {
                    appDataStore.enableDarkTheme(newTheme)
                }
            }

            AppSettingEvent.ToggleMasterNotification -> {
                val newStatus = !_state.value.isMasterNotificationEnabled
                _state.update { it.copy(isMasterNotificationEnabled = newStatus) }
                viewModelScope.launch {
                    userDataStore.setMasterNotificationEnabled(newStatus)
                }
            }

            AppSettingEvent.GoToRate -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = "https://play.google.com/store/apps/details?id=${context.packageName}".toUri()
                    setPackage("com.android.vending")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                _state.update { it.copy(isRatingDialogOpen = false) }
            }

            AppSettingEvent.ShareApp -> {
                val text = "Check out Islam 24 app: https://play.google.com/store/apps/details?id=${context.packageName}"
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val shareIntent = Intent.createChooser(intent, null).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(shareIntent)
            }

            AppSettingEvent.SupportIslam24 -> {
                val intent = Intent(Intent.ACTION_VIEW,
                    "https://islam24..top/support".toUri()).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }

            AppSettingEvent.OpenRatingDialog -> {
                _state.update { it.copy(isRatingDialogOpen = !it.isRatingDialogOpen) }
            }

            is AppSettingEvent.RateUs -> {
                val reviewManager = ReviewManagerFactory.create(context)
                val request = reviewManager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val reviewInfo = task.result
                        reviewManager.launchReviewFlow(event.activity, reviewInfo)
                            .addOnCompleteListener { launchTask ->
                                if (launchTask.exception == null) {
                                    _state.update { it.copy(isRatingDialogOpen = true) }
                                }
                            }
                    }
                }
            }

            AppSettingEvent.LogOut -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    try {
                        when (val result = logoutUseCase()) {
                            is Result.Success -> {
                                // Successful logout updates login status which resets navigation
                            }
                            is Result.Error -> {
                                val errorMessage = when (result.error) {
                                    AuthError.NO_INTERNET -> "No internet connection. Local session cleared."
                                    else -> "Logout failed on server, local session cleared."
                                }
                                _effect.emit(ProfileEffect.Error(errorMessage))
                            }
                        }
                    } catch (e: Exception) {
                        _effect.emit(ProfileEffect.Error(e.message ?: "An unexpected error occurred during logout"))
                    } finally {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }
}