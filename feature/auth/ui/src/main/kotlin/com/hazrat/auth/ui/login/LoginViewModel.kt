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
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    val isLogin = googleSignInUseCase(context = event.context)
                    if (isLogin) {
                        _effect.emit(LoginEffect.NavigateBack)
                    } else {
                        _effect.emit(LoginEffect.Error("Failed to login"))
                    }
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

}