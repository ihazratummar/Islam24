package com.hazrat.usecase.profile

import android.content.Context
import com.hazrat.domain.repository.AuthRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError

/**
 * @author hazratummar
 * Created on 06/08/26
 */
class GoogleSignInUseCase (
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(context: Context) : Result<Unit, AuthError> {
        return authRepository.googleCredentialSignIn(context = context)
    }
}