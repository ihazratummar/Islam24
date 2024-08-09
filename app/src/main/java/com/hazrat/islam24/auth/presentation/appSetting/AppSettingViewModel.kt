package com.hazrat.islam24.auth.presentation.appSetting

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hazrat.islam24.util.DataStorePreference
import com.hazrat.islam24.util.Languages
import com.hazrat.islam24.util.changeLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */


@HiltViewModel
class AppSettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {


    private val _state = MutableStateFlow(
        AppSettingState(
            currentLanguage = DataStorePreference.getLanguage(context = context),
            isDarkMode = DataStorePreference.getThemeMode(context)
        )
    )
    val appSettingState = _state.asStateFlow()


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
                        it.copy(currentLanguage = newLanguage.name)
                    }
                    DataStorePreference.setLanguage(context = context, language = newLanguage.name)
                    changeLanguage(context = context, language = newLanguage)
                }
            }

            is AppSettingEvent.ChangeTheme -> {
                viewModelScope.launch {
                    when (event.theme) {
                        Themes.SYSTEM -> {
                            _state.update {
                                it.copy(isDarkMode = systemTheme)
                            }
                            DataStorePreference.setThemeMode(context, systemTheme)
                        }

                        Themes.DARK -> {
                            _state.update {
                                it.copy(isDarkMode = true)
                            }
                            DataStorePreference.setThemeMode(context, true)
                        }

                        Themes.LIGHT -> {
                            _state.update {
                                it.copy(isDarkMode = false)
                            }
                            DataStorePreference.setThemeMode(context, false)
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

        }
    }

    private val systemTheme = when (Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> { true }
            Configuration.UI_MODE_NIGHT_NO -> { false }
            else -> false
        }

}