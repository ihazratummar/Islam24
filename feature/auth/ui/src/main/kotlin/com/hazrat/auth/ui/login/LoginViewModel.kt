package com.hazrat.auth.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hazrat.auth.domain.usecase.LoginUseCase
import com.hazrat.auth.domain.usecase.ObserveAuthStateUseCase
import com.hazrat.auth.domain.usecase.ObserveUserUseCase
import com.hazrat.model.AuthState
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val observeUserUseCase: ObserveUserUseCase,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {

    val authState: LiveData<AuthState> = observeAuthStateUseCase().asLiveData()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeUserUseCase().collect { user ->
                if (user != null) {
                    _uiEvent.send(UiEvent.NavigateToHome)
                }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> login()
            is LoginEvent.SetEmail -> {
                _loginState.update {
                    it.copy(
                        email = event.email,
                        isFormValid = isLoginFormValid(event.email, it.password)
                    )
                }
            }
            is LoginEvent.SetPassword -> {
                _loginState.update {
                    it.copy(
                        password = event.password,
                        isFormValid = isLoginFormValid(it.email, event.password)
                    )
                }
            }
            LoginEvent.OnPasswordVisibilityChanged -> {
                _loginState.update { it.copy(passwordVisible = !it.passwordVisible) }
            }
            LoginEvent.Refresh -> {
                // Not strictly needed with ObserveUserUseCase
            }
        }
    }

    private fun login() {
        val state = _loginState.value
        if (!isLoginFormValid(state.email, state.password)) {
            viewModelScope.launch { _uiEvent.send(UiEvent.ShowToast("Please fill all fields")) }
            return
        }

        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true) }
            val result = loginUseCase(state.email, state.password)
            _loginState.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    _loginState.update { it.copy(email = "", password = "") }
                    _uiEvent.send(UiEvent.NavigateToHome)
                }
                is Result.Error -> {
                    val message = mapAuthErrorToMessage(result.error)
                    _uiEvent.send(UiEvent.ShowToast(message))
                }
            }
        }
    }

    private fun isLoginFormValid(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun mapAuthErrorToMessage(error: AuthError): String {
        return when (error) {
            AuthError.EMAIL_ALREADY_IN_USE -> "Email already in use"
            AuthError.INVALID_CREDENTIALS -> "Invalid credentials"
            AuthError.USER_NOT_FOUND -> "User not found"
            AuthError.WRONG_PASSWORD -> "Wrong password"
            AuthError.WEAK_PASSWORD -> "Weak password"
            AuthError.NO_INTERNET -> "No internet connection"
            AuthError.UNKNOWN_ERROR -> "An unknown error occurred"
        }
    }

    sealed interface UiEvent {
        data class ShowToast(val message: String) : UiEvent
        data object NavigateToHome : UiEvent
    }
}