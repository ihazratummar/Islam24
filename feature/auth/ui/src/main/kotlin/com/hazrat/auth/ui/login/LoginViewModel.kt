package com.hazrat.auth.ui.login

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.model.profile.SupporterTickerModel
import com.hazrat.usecase.profile.GoogleSignInUseCase
import com.hazrat.usecase.profile.ListenToSupportTickerUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError

/**
 * @author hazratummar
 * Created on 06/08/26
 */
class LoginViewModel(
    private val googleSignInUseCase: GoogleSignInUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun event(event: LoginEvent) {
        when (event) {
            is LoginEvent.GoogleSignInClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    try {
                        when (val result = googleSignInUseCase(context = event.context)) {
                            is Result.Success -> {
                                _effect.emit(LoginEffect.NavigateBack)
                            }
                            is Result.Error -> {
                                val errorMessage = when (result.error) {
                                    AuthError.NO_INTERNET -> "No internet connection. Please check your network."
                                    AuthError.INVALID_CREDENTIALS -> "Invalid credentials. Please try again."
                                    else -> "Failed to login. Please try again."
                                }
                                _effect.emit(LoginEffect.Error(errorMessage))
                            }
                        }
                    } catch (e: Exception) {
                        _effect.emit(LoginEffect.Error(e.message ?: "An unexpected error occurred"))
                    } finally {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }
}