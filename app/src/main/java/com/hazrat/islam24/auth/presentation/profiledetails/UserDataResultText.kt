package com.hazrat.islam24.auth.presentation.profiledetails

import com.hazrat.ui.R
import com.hazrat.islam24.auth.presentation.UiText
import com.hazrat.islam24.util.error.Result
import com.hazrat.islam24.util.error.UserDataError
import com.hazrat.islam24.util.error.UserDataSuccess

/**
 * @author Hazrat Ummar Shaikh
 */

fun UserDataError.asUiText(): UiText {
    return when(this){
        UserDataError.NO_INTERNET -> {
            UiText.StringResource(id = R.string.check_internet)
        }
        UserDataError.INVALID_NAME -> {
            UiText.StringResource(id = R.string.invalid_name)
        }
        UserDataError.INVALID_BIO -> {
            UiText.StringResource(id = R.string.invalid_bio)
        }
        UserDataError.INVALID_USER_ID -> {
            UiText.StringResource(id = R.string.user_id)
        }
        UserDataError.UNKNOWN_ERROR -> {
            UiText.StringResource(id = R.string.unknown_error)
        }
    }
}

fun Result.Error<*, UserDataError>.asErrorUiText(): UiText {
    return error.asUiText()
}


fun UserDataSuccess.asSuccessUiText(): UiText {
    return when(this){
        UserDataSuccess.SUCCESS_NAME_UPDATE -> {
            UiText.StringResource(id = R.string.success_name_update)
        }

        UserDataSuccess.SUCCESS_BIO_UPDATE -> {
            UiText.StringResource(id = R.string.success_bio_update)
        }
    }
}

fun Result.Success<UserDataSuccess, *>.asSuccessUiText(): UiText {
    return data.asSuccessUiText()
}