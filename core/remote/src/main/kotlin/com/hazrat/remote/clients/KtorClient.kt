package com.hazrat.remote.clients

import android.util.Log
import com.hazrat.datastore.TokenStorage
import com.hazrat.remote.dto.auth.AuthResponse
import com.hazrat.remote.dto.auth.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okhttp3.Protocol
import kotlin.time.Duration.Companion.seconds

object KtorClient {

    // Dedicated Mutex to serialize all token refresh operations across parallel coroutines
    private val refreshMutex = Mutex()

    // Persistent lightweight client for refresh operations to avoid leaking OkHttpClient instances
    private val refreshClient by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    // Toggle between local and prod in this single location:
//     const val BASE_URL = "http://192.168.0.122:8080/api/v1/"
    const val BASE_URL = "https://api.islam24.app/api/v1/"

    val WS_BASE_URL: String
        get() = BASE_URL
            .replace("https://", "wss://")
            .replace("http://", "ws://")

    val ORIGIN_URL: String
        get() {
            return try {
                val uri = java.net.URI(BASE_URL)
                val portSuffix = if (uri.port != -1) ":${uri.port}" else ""
                "${uri.scheme}://${uri.host}$portSuffix"
            } catch (e: Exception) {
                "https://api.islam24.app"
            }
        }

    fun createPublicHttpClient(): HttpClient {
        val okHttpClient = okhttp3.OkHttpClient.Builder()
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()

        return HttpClient(OkHttp) {
            engine {
                preconfigured = okHttpClient
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    },
                    contentType = ContentType.Any
                )
            }
            install(WebSockets) {
                pingInterval = 15_000.seconds
            }
            defaultRequest {
                url.takeFrom(BASE_URL)
            }
        }
    }

    fun createHttpClient(tokenStorage: TokenStorage) : HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation){
                json(
                    Json{
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    }
                )
            }

            install(WebSockets){
                pingInterval = 15_000.seconds
            }

            defaultRequest {
                url.takeFrom(BASE_URL)
                contentType(ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    // Proactively attach Authorization header on all requests to avoid unnecessary 401 rounds
                    sendWithoutRequest { true }

                    loadTokens {
                        val accessToken = tokenStorage.getAccessToken()
                        val refreshToken = tokenStorage.getRefreshToken()
                        if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                            BearerTokens(accessToken, refreshToken)
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        // Serialize all refresh calls through Mutex so parallel 401s queue up
                        refreshMutex.withLock {
                            val storageAccessToken = tokenStorage.getAccessToken()
                            val storageRefreshToken = tokenStorage.getRefreshToken()

                            // 1. Double-Check: If another thread already refreshed the tokens while this thread
                            // was waiting for the lock, storage already has a NEW access token that differs from oldTokens.
                            if (!storageAccessToken.isNullOrBlank() && !storageRefreshToken.isNullOrBlank()
                                && (oldTokens == null || storageAccessToken != oldTokens?.accessToken)) {
                                Log.d("KtorClient", "Tokens were already refreshed by another concurrent request. Reusing new tokens.")
                                return@withLock BearerTokens(storageAccessToken, storageRefreshToken)
                            }

                            // 2. Always use the most up-to-date refreshToken from storage, falling back to oldTokens
                            val tokenToRefreshWith = storageRefreshToken?.takeIf { it.isNotBlank() }
                                ?: oldTokens?.refreshToken
                                ?: return@withLock null

                            try {
                                Log.d("KtorClient", "Initiating single serialized token refresh with backend...")
                                val response = refreshClient.post("${BASE_URL}auth/refresh") {
                                    contentType(ContentType.Application.Json)
                                    setBody(RefreshTokenRequest(refreshToken = tokenToRefreshWith))
                                }

                                when (response.status) {
                                    HttpStatusCode.OK, HttpStatusCode.Created, HttpStatusCode.Accepted -> {
                                        val authResponse = response.body<AuthResponse>()
                                        tokenStorage.saveTokens(
                                            accessToken = authResponse.accessToken,
                                            refreshToken = authResponse.refreshToken
                                        )
                                        Log.d("KtorClient", "Token refresh successful. New tokens saved to disk.")
                                        BearerTokens(authResponse.accessToken, authResponse.refreshToken)
                                    }
                                    HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> {
                                        // ONLY clear local session if backend explicitly rejects the refresh token with 401/403
                                        Log.w("KtorClient", "Refresh token explicitly rejected by backend (HTTP ${response.status.value}). Clearing session.")
                                        tokenStorage.clearToken()
                                        null
                                    }
                                    else -> {
                                        // For 400 Bad Request, 500, 502, 503, 429, etc.:
                                        // DO NOT log out user! Return existing tokens so user remains logged in.
                                        Log.w("KtorClient", "Non-auth error during token refresh (HTTP ${response.status.value}). Keeping tokens.")
                                        oldTokens
                                    }
                                }
                            } catch (e: java.io.IOException) {
                                // Network error (offline, timeout, airplane mode): DO NOT log out user!
                                Log.w("KtorClient", "Network error during token refresh: ${e.message}. Keeping session intact.")
                                oldTokens
                            } catch (e: Exception) {
                                Log.w("KtorClient", "Unexpected error during token refresh: ${e.message}. Keeping session intact.")
                                oldTokens
                            }
                        }
                    }
                }
            }
        }
    }
}