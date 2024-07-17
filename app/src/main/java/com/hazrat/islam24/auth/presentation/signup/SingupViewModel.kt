package com.hazrat.islam24.auth.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.islam24.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class SingupViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _state = MutableStateFlow(SingupState())
    val state: StateFlow<SingupState> = _state


    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    private fun isFormValid(): Boolean {
        return _state.value.name.isNotEmpty() &&
                _state.value.email.isNotEmpty() &&
                _state.value.password.isNotEmpty() &&
                _state.value.confirmPassword.isNotEmpty()
    }

    private fun isPasswordValid(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword && password.length >= 8
    }

    fun onEvent(event: SingupEvent) {
        when (event) {
            is SingupEvent.Signup -> {
                val name = _state.value.name
                val email = _state.value.email
                val password = _state.value.password
                val confirmPassword = _state.value.confirmPassword
                viewModelScope.launch {
                    singup(
                        name = name,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword
                    )
                }
                _state.update {
                    it.copy(
                        name = "",
                        email = "",
                        password = "",
                        confirmPassword = ""
                    )
                }
            }

            is SingupEvent.SetName -> {
                _state.update {
                    val newSTate = it.copy(
                        name = event.name,
                        isFormValid = isFormValid()
                    )
                    newSTate
                }
            }

            is SingupEvent.SetEmail -> {
                _state.update {
                    val newState = it.copy(
                        email = event.email,
                        isFormValid = isFormValid()
                    )
                    newState
                }
            }

            is SingupEvent.SetPassword -> {
                _state.update {
                    val newState = it.copy(
                        password = event.password,
                        isFormValid = isFormValid(),
                        isPasswordValid = isPasswordValid(event.password, it.confirmPassword)
                    )
                    newState
                }
            }

            is SingupEvent.SetConfirmPassword -> {
                _state.update {
                    val newState = it.copy(
                        confirmPassword = event.confirmPassword,
                        isFormValid = isFormValid(),
                        isPasswordValid = isPasswordValid(it.password, event.confirmPassword)
                    )
                    newState
                }
            }

            SingupEvent.OnPasswordVisibilityChanged -> {
                _state.update {
                    it.copy(
                        passwordVisible = !it.passwordVisible
                    )
                }
            }
            SingupEvent.OnConfirmPasswordVisibilityChanged -> {
                _state.update {
                    it.copy(
                        confirmPasswordVisible = !it.confirmPasswordVisible
                    )
                }
            }

            SingupEvent.Refresh -> {
                checkAuthStatus()
            }
        }
    }

    fun singup(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }
        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(it.exception?.message.toString())
                }
            }
    }
}