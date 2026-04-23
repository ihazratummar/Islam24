package com.hazrat.auth.ui.forgetPassword

import com.hazrat.ui.R
import com.hazrat.ui.UiText.UiText
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.ForgetPasswordError
import com.hazrat.utils.result.error.ForgetPasswordSuccess

fun ForgetPasswordError.asUiText(): UiText {
    return when(this){
        ForgetPasswordError.INVALID_EMAIL ->{
            UiText.StringResource(id = R.string.invalid_email)
        }

        ForgetPasswordError.NO_INTERNET -> {
            UiText.StringResource(id = R.string.check_internet)
        }
        ForgetPasswordError.INVALID_OTP -> {
            UiText.StringResource(id = R.string.invalid_otp)
        }
        ForgetPasswordError.INVALID_PASSWORD -> {
            UiText.StringResource(id = R.string.invalid_password)
        }
        ForgetPasswordError.UNKNOWN_ERROR -> {
            UiText.StringResource(id = R.string.unknown_error)
        }
    }
}

fun Result.Error<*, ForgetPasswordError>.asErrorUiText(): UiText {
    return error.asUiText()
}


fun Result.Success<ForgetPasswordSuccess, *>.asSuccessUiText(): UiText {
    return data.asSuccessUiText()
}

fun ForgetPasswordSuccess.asSuccessUiText(): UiText {
    return when(this){
        ForgetPasswordSuccess.SUCCESS_EMAIL_SENT -> {
            UiText.StringResource(id = R.string.email_sent_successfully)
        }
        ForgetPasswordSuccess.SUCCESS_EMAIL_VERIFIED -> {
            UiText.StringResource(id = R.string.email_verified_successfully)
        }
        ForgetPasswordSuccess.SUCCESS_PASSWORD_RESET -> {
            UiText.StringResource(id = R.string.password_reset_successfully)
        }
    }
}
