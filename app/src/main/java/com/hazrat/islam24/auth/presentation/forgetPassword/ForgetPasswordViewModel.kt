package com.hazrat.islam24.auth.presentation.forgetPassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.presentation.UiText
import com.hazrat.islam24.auth.repository.ForgetPasswordRepository
import com.hazrat.islam24.util.error.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val forgetPasswordRepository: ForgetPasswordRepository
) : ViewModel() {

    private val _forgetPasswordState = MutableStateFlow(ForgetPasswordState())
    val forgetPasswordState = _forgetPasswordState.asStateFlow()

    private val eventChannel = Channel<ForgetPasswordChannelEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            forgetPasswordRepository.networkObserver()
        }
    }

    fun onEvent(event: ForgetPasswordEvent) {
        when (event) {
            is ForgetPasswordEvent.EnterEmail -> {
                _forgetPasswordState.update {
                    it.copy(
                        email = event.email
                    )
                }
            }

            is ForgetPasswordEvent.SubmitEmail -> {
                _forgetPasswordState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                viewModelScope.launch {
                    when (val result = forgetPasswordRepository.requestOtp(event.email)) {
                        is Result.Success -> {
                            _forgetPasswordState.update {
                                it.copy(currentStep = 1, isLoading = false)
                            }
                            Log.d("ForgetPasswordViewModel", "onEvent: ${_forgetPasswordState.value.currentStep}")
                            val success = result.data.asSuccessUiText()
                            eventChannel.send(ForgetPasswordChannelEvent.Success(success))
                        }

                        is Result.Error -> {
                            _forgetPasswordState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                            val error = result.error.asUiText()
                            eventChannel.send(ForgetPasswordChannelEvent.Error(error))
                        }
                    }
                }
            }

            is ForgetPasswordEvent.EnterOtp -> {
                _forgetPasswordState.update {
                    it.copy(
                        otp = event.otp
                    )
                }
            }
            is ForgetPasswordEvent.SubmitOtp -> {
                _forgetPasswordState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                viewModelScope.launch {
                    when(val result = forgetPasswordRepository.verifyOtp(email = event.email, otp = event.otp)){
                        is Result.Error ->{
                            _forgetPasswordState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                            val error = result.error.asUiText()
                            eventChannel.send(ForgetPasswordChannelEvent.Error(error))
                        }
                        is Result.Success -> {
                            _forgetPasswordState.update {
                                it.copy(currentStep = 2, isLoading = false)
                            }
                            val success = result.data.asSuccessUiText()
                            eventChannel.send(ForgetPasswordChannelEvent.Success(success))
                        }
                    }
                }
            }
            is ForgetPasswordEvent.EnterPassword -> {
                _forgetPasswordState.update {
                    val newState = it.copy(
                        password = event.password,
                        isPasswordValid = isPasswordValid(event.password, it.confirmPassword)
                    )
                    newState
                }
            }
            is ForgetPasswordEvent.EnterConfirmPassword -> {
                _forgetPasswordState.update {
                    val newState = it.copy(
                        confirmPassword = event.confirmPassword,
                        isPasswordValid = isPasswordValid(it.password, event.confirmPassword)
                    )
                    newState
                }
            }
            is ForgetPasswordEvent.SubmitPassword -> {
                _forgetPasswordState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                viewModelScope.launch {
                    when(val result = forgetPasswordRepository.resetPassword(event.email, event.password)){
                        is Result.Error -> {
                            _forgetPasswordState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                            val error = result.error.asUiText()
                            eventChannel.send(ForgetPasswordChannelEvent.Error(error))
                        }
                        is Result.Success -> {
                            _forgetPasswordState.update {
                                it.copy(passwordUpdateSuccess = true, isLoading = false)
                            }
                            val success = result.data.asSuccessUiText()
                            eventChannel.send(ForgetPasswordChannelEvent.Success(success))
                        }
                    }
                }
            }

            ForgetPasswordEvent.StepBack -> {
                _forgetPasswordState.update {
                    it.copy(currentStep = it.currentStep - 1)
                }
            }

            ForgetPasswordEvent.OnPasswordVisibilityToggle -> {
                _forgetPasswordState.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible)
                }
            }
            ForgetPasswordEvent.OnConfirmPasswordVisibilityToggle -> {
                _forgetPasswordState.update {
                    it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
                }
            }
        }
    }
    private fun isPasswordValid(password: String, confirmPassword: String): Boolean {
        val passwordRegex = Regex(
            """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*#?&])[A-Za-z0-9@$!%*#?&]{8,}$"""
        )
        return passwordRegex.matches(password) && password == confirmPassword
    }
}


sealed interface ForgetPasswordChannelEvent {
    data class Error(val error: UiText) : ForgetPasswordChannelEvent
    data class Success(val success: UiText) : ForgetPasswordChannelEvent
}