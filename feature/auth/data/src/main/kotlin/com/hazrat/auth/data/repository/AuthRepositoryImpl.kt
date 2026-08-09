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
import com.hazrat.datastore.TokenStorage
import com.hazrat.domain.repository.AuthRepository
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.remote.api.AuthApiCall
import com.hazrat.remote.api.profile.ProfileApi
import com.hazrat.remote.dto.auth.GoogleLoginRequest
import com.hazrat.remote.dto.auth.LogoutRequest
import com.revenuecat.purchases.Purchases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError

/**
 * Production Industry-Grade Authentication Repository Implementation.
 * Encapsulates Google Sign-In via Credential Manager, secure token storage, and remote/credential logout.
 *
 * @author hazratummar
 */
class AuthRepositoryImpl(
    private val credentialManager: CredentialManager,
    private val authApiCall: AuthApiCall,
    private val tokenStorage: TokenStorage,
    private val profileApi: ProfileApi,
    private val profileRepository: ProfileRepository
) : AuthRepository {

    override suspend fun googleCredentialSignIn(context: Context): Result<Unit, AuthError> = withContext(Dispatchers.IO) {
        try {
            val response = buildCredentialResponse(context = context)
            handleSignIn(result = response)
        } catch (e: Exception) {
            Log.e("AuthImpl", "Google Sign IN failed: $e")
            Result.Error(AuthError.UNKNOWN_ERROR)
        }
    }

    private suspend fun buildCredentialResponse(context: Context): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(false) // Force Google Account Picker to appear so user can switch accounts!
                    .setServerClientId(BuildConfig.GOOGLE_SIGN_WEB_SDK_CLIENT)
                    .build()
            ).build()
        return credentialManager.getCredential(context = context, request = request)
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Result<Unit, AuthError> {
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
                    return Result.Success(Unit)
                } else {
                    Log.e("AuthImpl", "Server Rejected token")
                    return Result.Error(AuthError.INVALID_CREDENTIALS)
                }
            } catch (e: Exception) {
                Log.e("AuthImpl", "Server Error sending token: $e")
                return Result.Error(AuthError.NO_INTERNET)
            }
        } else {
            Log.e(
                "AuthImpl",
                "Credential was not a Google ID Token: ${credential.javaClass.simpleName}"
            )
            return Result.Error(AuthError.INVALID_CREDENTIALS)
        }
    }

    override suspend fun logout(): Result<Unit, AuthError> = withContext(Dispatchers.IO) {
        val refreshToken = tokenStorage.getRefreshToken()
        var serverSuccess = false
        if (refreshToken != null) {
            try {
                serverSuccess = authApiCall.logout(request = LogoutRequest(refreshToken = refreshToken))
            } catch (e: Exception) {
                Log.e("AuthImpl", "Server logout failed: $e")
            }
        }

        // ALWAYS clear Google Credential Manager state so device forgets previous account selection
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.e("AuthImpl", "Failed to clear credential manager state: $e")
        }

        // ALWAYS log out RevenueCat user session
        if (Purchases.isConfigured) {
            try {
                Purchases.sharedInstance.logOut()
            } catch (e: Exception) {
                Log.e("AuthImpl", "RevenueCat logOut failed: $e")
            }
        }

        if (serverSuccess) {
            Result.Success(Unit)
        } else {
            Result.Error(AuthError.UNKNOWN_ERROR)
        }
    }

    override suspend fun clearLocalSession(): Unit = withContext(Dispatchers.IO) {
        tokenStorage.clearToken()
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.e("AuthImpl", "Failed to clear credential state in clearLocalSession: $e")
        }
    }
}