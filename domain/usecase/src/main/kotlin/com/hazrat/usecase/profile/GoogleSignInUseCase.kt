package com.hazrat.usecase.profile

import android.content.Context
import com.hazrat.domain.repository.AuthRepository


/**
 * @author hazratummar
 * Created on 06/08/26
 */

class GoogleSignInUseCase (
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(context: Context) : Boolean {
        return authRepository.googleCredentialSignIn(context = context)
    }
}