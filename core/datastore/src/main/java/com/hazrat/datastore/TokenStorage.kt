package com.hazrat.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Encrypted Storage for Auth Tokens with Reactive Flow support.
 * @author hazratummar
 */
@Suppress("DEPRECATION")
class TokenStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit {
            putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refresh_token", null)
    }

    fun clearToken() {
        sharedPreferences.edit {
            remove("access_token")
            remove("refresh_token")
            clear()
        }
    }

    /**
     * Reactive Flow emitting true when a valid refresh_token exists, and false when cleared.
     */
    val isLoggedIn: Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == "refresh_token" || key == "access_token" || key == null) {
                trySend(!prefs.getString("refresh_token", null).isNullOrBlank())
            }
        }

        // Emit initial value on subscription
        trySend(!sharedPreferences.getString("refresh_token", null).isNullOrBlank())

        // Register listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        // Unregister when collection ends to avoid memory leaks
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
}