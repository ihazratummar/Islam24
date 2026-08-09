package com.hazrat.domain.repository

import android.content.Context


/**
 * @author hazratummar
 * Created on 05/08/26
 */

import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError

interface AuthRepository {

    suspend fun googleCredentialSignIn(context: Context) : Result<Unit, AuthError>

    suspend fun logout() : Result<Unit, AuthError>

    suspend fun clearLocalSession()

}