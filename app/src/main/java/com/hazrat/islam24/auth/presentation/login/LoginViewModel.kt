package com.hazrat.islam24.auth.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.core.domain.repository.QuranRepository
import com.hazrat.islam24.core.domain.repository.ZakatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val zakatRepository: ZakatRepository,
    private val profileRepository: ProfileRepository,
    private val storage: FirebaseStorage,
    private val quranRepository: QuranRepository
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
            syncData()
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
                checkAuthStatus()
            }
        }
    }

    private suspend fun login(email: String, password: String) {
        if (!isLoginFormValid(_loginState.value)) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }

        _authState.value = AuthState.Loading
        delay(2000L)
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
                    syncData()
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val storageRef = storage.reference
                    val imageRef = storageRef.child("image/$userId/profile_image")
                    imageRef.downloadUrl.addOnSuccessListener{
                        profileRepository.saveProfilePictureLocally(uri = it)
                    }
                } else {
                    viewModelScope.launch {
                        _authState.value = AuthState.Loading
                        delay(1000L)
                        _authState.value =
                            AuthState.Error(task.exception?.message ?: "Authentication failed")
                    }
                }
            }
            .addOnFailureListener { e ->
                viewModelScope.launch {
                    _authState.value = AuthState.Loading
                    delay(1000L)
                    _authState.value = AuthState.Error(e.message ?: "Authentication failed")
                }
            }
    }
    private fun syncData(){
        syncZakatData()
        syncQuranData()
    }

    private fun syncZakatData(){
        viewModelScope.launch(SupervisorJob()){
            zakatRepository.syncData()
        }
    }

    private fun syncQuranData(){
        viewModelScope.launch(SupervisorJob()){
            quranRepository.syncQuranDataOnLogin()
        }
    }
}