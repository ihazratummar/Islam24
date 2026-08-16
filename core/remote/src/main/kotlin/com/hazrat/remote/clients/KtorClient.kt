package com.hazrat.remote.clients

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
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

import okhttp3.Protocol

object KtorClient {

    // Toggle between local and prod in this single location:
     const val BASE_URL = "http://192.168.0.122:8080/api/v1/"
//    const val BASE_URL = "https://api.islam24.app/api/v1/"

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
            engine {
                addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    val token = tokenStorage.getAccessToken()
                    if (!token.isNullOrBlank()) {
                        requestBuilder.header("Authorization", "Bearer $token")
                    } else {
                        requestBuilder.removeHeader("Authorization")
                    }
                    chain.proceed(requestBuilder.build())
                }
            }

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

            install(Auth){
                bearer {
                    loadTokens {
                        val accessToken = tokenStorage.getAccessToken()
                        val refreshToken = tokenStorage.getRefreshToken()
                        if (accessToken != null && refreshToken != null){
                            BearerTokens(accessToken, refreshToken)
                        }else{
                            null
                        }
                    }

                    refreshTokens {
                        try {

                            val storageAccessToken = tokenStorage.getAccessToken()
                            val storageRefreshToken = tokenStorage.getRefreshToken()

                            // 1. If storage has new tokens that differ from the failed request, use them directly
                            if (storageAccessToken != null && storageRefreshToken != null
                                && (oldTokens == null || storageAccessToken != oldTokens?.accessToken)){
                                return@refreshTokens BearerTokens(storageAccessToken, storageRefreshToken)
                            }

                            // 2. Otherwise, use the storage refresh token or fallback to oldTokens
                            val refreshToken = storageRefreshToken ?: oldTokens?.refreshToken
                            if (refreshToken == null){
                                tokenStorage.clearToken()
                                return@refreshTokens null
                            }

                            val refreshClient = HttpClient(OkHttp){
                                install(ContentNegotiation){json(Json { ignoreUnknownKeys = true })}
                            }

                            val response = refreshClient.post("${BASE_URL}auth/refresh"){
                                contentType(ContentType.Application.Json)
                                setBody(RefreshTokenRequest(refreshToken = refreshToken))
                            }

                            if (response.status.value in 200..299){
                                val authResponse = response.body<AuthResponse>()
                                tokenStorage.saveTokens(
                                    accessToken = authResponse.accessToken,
                                    refreshToken = authResponse.refreshToken
                                )
                                BearerTokens(authResponse.accessToken, authResponse.refreshToken)
                            }else{
                                tokenStorage.clearToken()
                                null
                            }
                        }catch (e: Exception){
                            tokenStorage.clearToken()
                            null
                        }
                    }
                }
            }
        }
    }

}