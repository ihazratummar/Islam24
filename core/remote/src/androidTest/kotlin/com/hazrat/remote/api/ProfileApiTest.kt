package com.hazrat.remote.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazrat.remote.api.auth.AuthApiImple
import com.hazrat.remote.dto.auth.GoogleLoginRequest
import com.hazrat.remote.dto.auth.LogoutRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented Integration Test for Auth/Profile Ktor API calls.
 * @author hazratummar
 */
@RunWith(AndroidJUnit4::class)
class ProfileApiTest {

    private val testBaseUrl = "https://api.islam24app/api/v1/"

    @Test
    fun testGoogleLogin_Success_ReturnsTokens() = runBlocking {
        // Arrange MockEngine for success response (200 OK)
        val mockEngine = MockEngine { request ->
            assertEquals("https://api.islam24.app/api/v1/auth/google", request.url.toString())
            respond(
                content = """{"accessToken":"test_access_token_123","refreshToken":"test_refresh_token_456"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val testHttpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            defaultRequest {
                url.takeFrom(testBaseUrl)
                contentType(ContentType.Application.Json)
            }
        }

        val authApiCall: AuthApiCall = AuthApiImple(httpClient = testHttpClient)

        // Act
        val response = authApiCall.googleLogin(GoogleLoginRequest(idToken = "mock_google_id_token"))

        // Assert
        assertNotNull("AuthResponse should not be null on success", response)
        assertEquals("test_access_token_123", response?.accessToken)
        assertEquals("test_refresh_token_456", response?.refreshToken)
    }

    @Test
    fun testGoogleLogin_Failure_ReturnsNull() = runBlocking {
        // Arrange MockEngine for failure response (401 Unauthorized)
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"error":"Invalid Google Token"}""",
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val testHttpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            defaultRequest {
                url.takeFrom(testBaseUrl)
                contentType(ContentType.Application.Json)
            }
        }

        val authApiCall: AuthApiCall = AuthApiImple(httpClient = testHttpClient)

        // Act
        val response = authApiCall.googleLogin(GoogleLoginRequest(idToken = "invalid_token"))

        // Assert
        assertNull("AuthResponse should be null when login fails", response)
    }

    @Test
    fun testLogout_Success_ReturnsTrue() = runBlocking {
        // Arrange MockEngine for 204 No Content response
        val mockEngine = MockEngine { request ->
            assertEquals("https://api.islam24.app/api/v1/auth/logout", request.url.toString())
            respond(
                content = "",
                status = HttpStatusCode.NoContent,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val testHttpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            defaultRequest {
                url.takeFrom(testBaseUrl)
                contentType(ContentType.Application.Json)
            }
        }

        val authApiCall: AuthApiCall = AuthApiImple(httpClient = testHttpClient)

        // Act
        val isSuccess = authApiCall.logout(LogoutRequest(refreshToken = "test_refresh_token"))

        // Assert
        assertTrue("Logout should return true on 204 No Content", isSuccess)
    }
}