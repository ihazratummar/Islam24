package com.hazrat.remote.clients

import com.hazrat.datastore.TokenStorage
import com.hazrat.remote.dto.auth.AuthResponse
import com.hazrat.remote.dto.auth.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {

    private val BASE_URL = "http://192.168.0.122:8080"

    fun createHttpClient(tokenStorage: TokenStorage) : HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation){
                json(
                    Json{
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    }
                )
            }


            defaultRequest {
                url(BASE_URL)
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
                            val refreshToken = oldTokens?.refreshToken
                            if (refreshToken == null){
                                tokenStorage.clearToken()
                                return@refreshTokens null
                            }

                            val refreshClient = HttpClient(Android){
                                install(ContentNegotiation){json(Json { ignoreUnknownKeys = true })}
                            }

                            val response = refreshClient.post("$BASE_URL/api/v1/auth/refresh"){
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