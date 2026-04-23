package com.hazrat.auth.ui.signup

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */


class SignUpViewModel (
    private val context: Context,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val authState: LiveData<AuthState> = profileRepository.authState

    private val _state = MutableStateFlow(SingupState())
    val state: StateFlow<SingupState> = _state


    init {
        viewModelScope.launch {
            profileRepository.checkAuthStatus()
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
                    signup(
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
                viewModelScope.launch {
                    profileRepository.checkAuthStatus()
                }

            }
        }
    }

    private suspend fun signup(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val result =  profileRepository.signup(
            name = name,
            email = email,
            password = password, confirmPassword = confirmPassword
        )
        if (result){
            Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Signup failed", Toast.LENGTH_SHORT).show()
        }
    }
}