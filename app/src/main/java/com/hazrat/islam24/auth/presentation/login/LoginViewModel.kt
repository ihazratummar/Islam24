package com.hazrat.islam24.auth.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.islam24.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {


    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState


    private fun isLoginFormValid(state: LoginState): Boolean {
        return state.email.isNotEmpty() && state.password.isNotEmpty()
    }

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

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> {
                val email = _loginState.value.email
                val password = _loginState.value.password
                login(email, password)

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
                checkAuthStatus()
            }
        }
    }

    private fun login(email: String, password: String) {
        if (!isLoginFormValid(_loginState.value)) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    _loginState.update {
                        it.copy(
                            email = "",
                            password = ""
                        )
                    }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Authentication failed")
                }

            }.addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Authentication failed")
            }
    }
}