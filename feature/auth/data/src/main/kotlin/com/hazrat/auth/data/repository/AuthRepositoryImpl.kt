package com.hazrat.auth.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hazrat.auth.data.BuildConfig
import com.hazrat.auth.data.mapper.toEntity
import com.hazrat.auth.data.mapper.toModel
import com.hazrat.database.dao.UserSupportStatusDao
import com.hazrat.datastore.TokenStorage
import com.hazrat.domain.repository.AuthRepository
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.remote.api.AuthApiCall
import com.hazrat.remote.api.profile.ProfileApi
import com.hazrat.remote.dto.auth.GoogleLoginRequest
import com.hazrat.remote.dto.auth.LogoutRequest
import com.revenuecat.purchases.Purchases
import kotlin.contracts.contract


/**
 * @author hazratummar
 * Created on 05/08/26
 */

class AuthRepositoryImpl(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val authApiCall: AuthApiCall,
    private val tokenStorage: TokenStorage,
    private val profileApi: ProfileApi,
    private val profileRepository: ProfileRepository,
    private val userSupportStatusDao: UserSupportStatusDao
) : AuthRepository {

    override suspend fun googleCredentialSignIn(context: Context): Boolean {
        return try {
            val response = buildCredentialResponse(context = context)
            handleSignIn(result = response)
        } catch (e: Exception) {
            Log.e("AuthImpl", "Google Sign IN failed")
            false
        }
    }

    private suspend fun buildCredentialResponse(context: Context): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.GOOGLE_SIGN_WEB_SDK_CLIENT)
                    .build()
            ).build()
        return credentialManager.getCredential(context = context, request = request)
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Boolean {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(data = credential.data)
                val googleIdToken = tokenCredential.idToken
                Log.d("AuthImpl", "Google Id Token $googleIdToken")
                val authResponse =
                    authApiCall.googleLogin(request = GoogleLoginRequest(idToken = googleIdToken))
                Log.d("AuthImpl", "AuthResponse ${authResponse?.accessToken}")
                val userId = tokenCredential.id
                if (authResponse != null) {
                    tokenStorage.saveTokens(
                        accessToken = authResponse.accessToken,
                        refreshToken = authResponse.refreshToken
                    )

                    val profileResponse = profileApi.getProfile()
                    if (profileResponse != null) {
                        profileRepository.insertUser(user = profileResponse.toEntity())
                    }

                    if (Purchases.isConfigured) {
                        try {
                            Purchases.sharedInstance.logIn(userId)
                        } catch (e: Exception) {
                            Log.e("AuthImpl", "RevenueCat logIn failed: $e")
                        }
                    }
                    return true
                } else {
                    Log.e("AuthImpl", "Server Rejected token")
                    return false
                }
            } catch (e: Exception) {
                Log.e("AuthImpl", "Server Error sending token: $e")
                return false
            }
        } else {
            Log.e(
                "AuthImpl",
                "Credential was not a Google ID Token: ${credential.javaClass.simpleName}"
            )
            return false
        }
    }

    override suspend fun logout(): Boolean {
        val refreshToken = tokenStorage.getRefreshToken() ?: return false
        val serverLogoutSuccessful = try {
            authApiCall.logout(request = LogoutRequest(refreshToken = refreshToken))
        } catch (e: Exception) {
            Log.e("AuthImpl", "Failed to logout $e")
            false
        }
        if (!serverLogoutSuccessful) {
            return false
        }
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.e("AuthImpl", "Failed to clear credential manager state: $e")
        }

        if (Purchases.isConfigured) {
            try {
                Purchases.sharedInstance.logOut()
            } catch (e: Exception) {
                Log.e("AuthImpl", "RevenueCat logOut failed: $e")
            }
        }
        return true
    }

    override suspend fun clearLocalSession() {
        tokenStorage.clearToken()
    }
}