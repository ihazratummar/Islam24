package com.hazrat.islam24.auth.presentation.appSetting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.util.DataStorePreference
import com.hazrat.islam24.util.changeLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */


@HiltViewModel
class AppSettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepository: ProfileRepository,
    private val dataStorePreference: DataStorePreference
) : ViewModel() {

    private val _state = MutableStateFlow(
        AppSettingState(
            currentLanguage = dataStorePreference.getLanguage(),
            isDarkMode = dataStorePreference.getThemeMode(),
            currentTheme = dataStorePreference.getThemeName(),
        )
    )
    val appSettingState = _state.asStateFlow()



    val authState: LiveData<AuthState> = profileRepository.authState
    init {
        profileRepository.checkAuthStatus()
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
                    dataStorePreference.setLanguage( language = newLanguage)
                    changeLanguage(language = newLanguage, context = context)
                }
            }
            is AppSettingEvent.ChangeTheme -> {
                viewModelScope.launch {
                    when (event.theme) {
                        Themes.DARK -> {
                            _state.update {
                                it.copy(
                                    isDarkMode = true,
                                    currentTheme = Themes.DARK
                                )
                            }
                            dataStorePreference.setThemeMode( true)
                            dataStorePreference.setThemeName( Themes.DARK)
                        }

                        Themes.LIGHT -> {
                            _state.update {
                                it.copy(
                                    isDarkMode = false,
                                    currentTheme = Themes.LIGHT
                                )
                            }
                            dataStorePreference.setThemeMode( false)
                            dataStorePreference.setThemeName( Themes.LIGHT)
                        }
                    }
                }
            }

            AppSettingEvent.ClickThemeDialog -> {
                _state.update {
                    it.copy(
                        isThemeDialogOpen = !it.isThemeDialogOpen
                    )
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
                }
            }

            AppSettingEvent.RefreshAuth -> {
                profileRepository.checkAuthStatus()
            }
        }
    }
}

