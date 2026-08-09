package com.hazrat.remote.api.apiData

import android.util.Log
import com.hazrat.remote.api.profile.SupporterWebSocketApi
import com.hazrat.remote.clients.KtorClient
import com.hazrat.remote.dto.LiveCommunityTickerPayload
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * @author hazratummar
 * Created on 08/08/26
 */

class SupporterWebSocketApiImpl : SupporterWebSocketApi {

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()

    override fun listenToCommunityUpdates(): Flow<LiveCommunityTickerPayload> = channelFlow {
        val fullWsUrl = "${KtorClient.WS_BASE_URL}ws-supporter"
        val originUrl = KtorClient.ORIGIN_URL

        val request = Request.Builder()
            .url(fullWsUrl)
            .header("Origin", originUrl)
            .build()

        val webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected to STOMP WebSocket successfully at $fullWsUrl")
                // 1. Send STOMP CONNECT frame
                webSocket.send("CONNECT\naccept-version:1.1,1.2\nheart-beat:0,0\n\n\u0000")
                // 2. Send STOMP SUBSCRIBE frame
                webSocket.send("SUBSCRIBE\nid:sub-0\ndestination:/topic/community-updates\n\n\u0000")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Raw incoming STOMP frame: $text")
                if (text.startsWith("MESSAGE") && text.contains("/topic/community-updates")) {
                    val jsonBody = text.substringAfter("\r\n\r\n")
                        .let { if (it == text) text.substringAfter("\n\n") else it }
                        .trimEnd('\u0000', '\n', '\r', ' ')
                    
                    Log.d("WebSocket", "Extracted JSON body: $jsonBody")
                    try {
                        val payload = json.decodeFromString<LiveCommunityTickerPayload>(jsonBody)
                        trySend(payload)
                    } catch (e: Exception) {
                        Log.e("WebSocket", "Failed to parse payload: ${e.message}")
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                val errorBody = response?.body?.string() ?: ""
                Log.e("WebSocket", "Websocket connection error: ${t.message}, code: ${response?.code}, body: $errorBody")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(code, reason)
            }
        })

        awaitClose {
            webSocket.close(1000, "Flow cancelled")
        }
    }
}