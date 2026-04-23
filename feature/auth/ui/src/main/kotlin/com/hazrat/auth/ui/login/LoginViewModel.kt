package com.hazrat.auth.ui.login

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


class LoginViewModel(
    private val profileRepository: ProfileRepository,
    private val context: Context,
) : ViewModel() {


    val authState: LiveData<AuthState> = profileRepository.authState

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState


    private fun isLoginFormValid(state: LoginState): Boolean {
        return state.email.isNotEmpty() && state.password.isNotEmpty()
    }

    init {
        viewModelScope.launch {
            profileRepository.checkAuthStatus()
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> {
                viewModelScope.launch {
                    val email = _loginState.value.email
                    val password = _loginState.value.password
                    login(email, password)
                }

            }


            is LoginEvent.SetEmail -> {
                _loginState.update {
                    val newState = it.copy(
                        email = event.email,
                        isFormValid = isLoginFormValid(it.copy(email = event.email))
                    )
                    newState
                }
            }

            is LoginEvent.SetPassword -> {
                _loginState.update {
                    val newState = it.copy(
                        password = event.password,
                        isFormValid = isLoginFormValid(it.copy(password = event.password))
                    )
                    newState
                }
            }

            LoginEvent.OnPasswordVisibilityChanged -> {
                _loginState.update {
                    it.copy(
                        passwordVisible = !it.passwordVisible
                    )
                }
            }

            LoginEvent.Refresh -> {
                viewModelScope.launch {
                    profileRepository.checkAuthStatus()
                }
            }
        }
    }

    private suspend fun login(email: String, password: String) {
        if (!isLoginFormValid(_loginState.value)) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        val result = profileRepository.login(email, password)
        if (result){
            _loginState.update {
                it.copy(email = "", password = "")
            }
        }else{
            Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
        }
    }

}