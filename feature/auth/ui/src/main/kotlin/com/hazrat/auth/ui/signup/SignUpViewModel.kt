package com.hazrat.auth.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hazrat.auth.domain.usecase.SignUpUseCase
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

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val observeUserUseCase: ObserveUserUseCase,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {

    val authState: LiveData<AuthState> = observeAuthStateUseCase().asLiveData()

    private val _state = MutableStateFlow(SingupState())
    val state: StateFlow<SingupState> = _state.asStateFlow()

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

    fun onEvent(event: SingupEvent) {
        when (event) {
            is SingupEvent.Signup -> signup()
            is SingupEvent.SetName -> {
                _state.update { it.copy(name = event.name, isFormValid = isFormValid(event.name, it.email, it.password, it.confirmPassword)) }
            }
            is SingupEvent.SetEmail -> {
                _state.update { it.copy(email = event.email, isFormValid = isFormValid(it.name, event.email, it.password, it.confirmPassword)) }
            }
            is SingupEvent.SetPassword -> {
                _state.update { 
                    it.copy(
                        password = event.password, 
                        isFormValid = isFormValid(it.name, it.email, event.password, it.confirmPassword),
                        isPasswordValid = isPasswordMatch(event.password, it.confirmPassword)
                    ) 
                }
            }
            is SingupEvent.SetConfirmPassword -> {
                _state.update { 
                    it.copy(
                        confirmPassword = event.confirmPassword, 
                        isFormValid = isFormValid(it.name, it.email, it.password, event.confirmPassword),
                        isPasswordValid = isPasswordMatch(it.password, event.confirmPassword)
                    ) 
                }
            }
            SingupEvent.OnPasswordVisibilityChanged -> {
                _state.update { it.copy(passwordVisible = !it.passwordVisible) }
            }
            SingupEvent.OnConfirmPasswordVisibilityChanged -> {
                _state.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
            }
            SingupEvent.Refresh -> {}
        }
    }

    private fun signup() {
        val currentState = _state.value
        if (!isFormValid(currentState.name, currentState.email, currentState.password, currentState.confirmPassword)) {
            viewModelScope.launch { _uiEvent.send(UiEvent.ShowToast("Please fill all fields correctly")) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = signUpUseCase(currentState.name, currentState.email, currentState.password)
            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    _uiEvent.send(UiEvent.ShowToast("Signup successful"))
                    _uiEvent.send(UiEvent.NavigateToHome)
                }
                is Result.Error -> {
                    _uiEvent.send(UiEvent.ShowToast(mapAuthErrorToMessage(result.error)))
                }
            }
        }
    }

    private fun isFormValid(name: String, email: String, password: String, confirmPassword: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword == password
    }

    private fun isPasswordMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword && password.isNotEmpty()
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