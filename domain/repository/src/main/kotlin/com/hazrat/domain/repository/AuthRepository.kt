package com.hazrat.domain.repository

import android.content.Context


/**
 * @author hazratummar
 * Created on 05/08/26
 */

interface AuthRepository {

    suspend fun googleCredentialSignIn(context: Context) : Boolean

    suspend fun logout() : Boolean

    suspend fun clearLocalSession()

}