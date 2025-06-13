package com.hazrat.islam24.auth.presentation.appSetting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileEvent
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.core.domain.repository.QuranRepository
import com.hazrat.islam24.util.changeLanguage
import com.hazrat.islam24.util.datastore.AppDataStore
import com.hazrat.islam24.util.datastore.DataStorePreference
import com.hazrat.islam24.util.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import com.google.android.play.core.review.ReviewManagerFactory
import com.hazrat.islam24.R

/**
 * @author Hazrat Ummar Shaikh
 */


@HiltViewModel
class AppSettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepository: ProfileRepository,
    private val dataStorePreference: DataStorePreference,
    private val quranRepository: QuranRepository,
    private val appDataStore: AppDataStore,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(
        AppSettingState(
            currentLanguage = dataStorePreference.getLanguage(),
        )
    )
    val appSettingState: StateFlow<AppSettingState> = combine(
        _state,
        appDataStore.isDarkModeEnabled,
        appDataStore.isHapticEnabled
    ) { state, isDarkModeEnabled, isHapticEnabled ->
        state.copy(
            toggleTheme = isDarkModeEnabled,
            isHapticFeedbackEnabled = isHapticEnabled
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AppSettingState()
    )


    val authState: LiveData<AuthState> = profileRepository.authState

    init {
        profileRepository.checkAuthStatus()

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

    }

    fun onAppSettingEvent(event: AppSettingEvent) {
        when (event) {
            AppSettingEvent.ClickLanguageDialog -> {
                _state.update {
                    it.copy(
                        isLanguageDialogOpen = !it.isLanguageDialogOpen
                    )
                }
            }

            is AppSettingEvent.SelectLanguage -> {
                viewModelScope.launch {
                    val newLanguage = event.language
                    _state.update {
                        it.copy(
                            currentLanguage = newLanguage,
                        )
                    }
                    dataStorePreference.setLanguage(language = newLanguage)
                    changeLanguage(language = newLanguage, context = context)
                }
            }

            AppSettingEvent.OpenAppSetting -> {
                val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }

            AppSettingEvent.SignOut -> {
                viewModelScope.launch {
                    profileRepository.signOut()
                    launch {
                        quranRepository.syncQuranDataIfLoggedIn()
                    }
                    launch {
                        userDataStore.clearSelectedCompassId()
                    }
                }
            }

            AppSettingEvent.RefreshAuth -> {
                profileRepository.checkAuthStatus()
            }

            AppSettingEvent.HapticFeedbackClick -> {
                _state.update {
                    it.copy(isHapticFeedbackEnabled = !it.isHapticFeedbackEnabled)
                }
                viewModelScope.launch {
                    appDataStore.enableHaptic(_state.value.isHapticFeedbackEnabled)
                }
            }

            AppSettingEvent.ToggleTheme -> {
                val newTheme = !_state.value.toggleTheme
                _state.update {
                    it.copy(toggleTheme = newTheme)
                }
                viewModelScope.launch {
                    appDataStore.enableDarkTheme(newTheme)
                }
            }

            AppSettingEvent.GoToRate -> {
                val intent: Intent = Intent(Intent.ACTION_VIEW).apply {
                    data =
                        "https://play.google.com/store/apps/details?id=${context.packageName}".toUri()
                    setPackage("com.android.vending")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                _state.update {
                    it.copy(
                        isRatingDialogOpen = false
                    )

                }
            }

            AppSettingEvent.InviteFriend -> {
                val text = context.getString(R.string.invite_friend)
                val intent: Intent = Intent().apply {
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

            AppSettingEvent.OpenRatingDialog -> {
                _state.update {
                    it.copy(isRatingDialogOpen = !it.isRatingDialogOpen)
                }
            }

            is AppSettingEvent.RateUs -> {
                val reviewManager = ReviewManagerFactory.create(context)
                val request = reviewManager.requestReviewFlow()

                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val reviewInfo = task.result
                        reviewManager.launchReviewFlow(event.activity, reviewInfo)
                            .addOnCompleteListener { launchTask ->
                                Log.d("ProfileRepositoryImpl", "rateUs: ${launchTask.result}")
                                if (launchTask.exception == null) {
                                    _state.update {
                                        it.copy(
                                            isRatingDialogOpen = true
                                        )
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

}

