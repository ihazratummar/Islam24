package com.hazrat.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.usecase.profile.GoogleSignInUseCase
import com.hazrat.usecase.profile.SyncDataUseCase
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author hazratummar
 * Created on 06/08/26
 */
class LoginViewModel(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val syncDataUseCase: SyncDataUseCase
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
                                // 1. Await full data sync & recovery directly so Room is populated
                                syncDataUseCase()
                                // 2. Navigate back to app
                                _effect.emit(LoginEffect.NavigateBack)
                            }
                            is Result.Error -> {
                                if (result.error == AuthError.USER_CANCELLED) {
                                    // User dismissed or cancelled the Google sheet, no error message needed
                                    return@launch
                                }
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