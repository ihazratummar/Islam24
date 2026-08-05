package com.hazrat.remote.api.apiData

import android.util.Log
import com.hazrat.remote.api.profile.SupporterWebSocketApi
import com.hazrat.remote.dto.LiveCommunityTickerPayload
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.json.Json

/**
 * @author hazratummar
 * Created on 08/08/26
 */

class SupporterWebSocketApiImpl(
    private val httpClient: HttpClient
) : SupporterWebSocketApi {

    private val json = Json { ignoreUnknownKeys = true }

    override fun listenToCommunityUpdates(): Flow<LiveCommunityTickerPayload> = channelFlow {
        try {
            httpClient.webSocket("ws-supporter") {
                // 1. Send STOMP CONNECT frame
                send(Frame.Text("CONNECT\naccept-version:1.1,1.2\nheart-beat:0,0\n\n\u0000"))
                // 2. Send STOMP SUBSCRIBE frame
                send(Frame.Text("SUBSCRIBE\nid:sub-0\ndestination:/topic/community-updates\n\n\u0000"))
                // 3. Process incoming STOMP frames
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        Log.d("WebSocket", "Raw incoming STOMP frame: $text")
                        if (text.startsWith("MESSAGE") && text.contains("/topic/community-updates")) {
                            // Extract JSON body cleanly handling both \r\n\r\n and \n\n STOMP headers separations
                            val jsonBody = text.substringAfter("\r\n\r\n")
                                .let { if (it == text) text.substringAfter("\n\n") else it }
                                .trimEnd('\u0000', '\n', '\r', ' ')
                            
                            Log.d("WebSocket", "Extracted JSON body: $jsonBody")
                            try {
                                val payload = json.decodeFromString<LiveCommunityTickerPayload>(jsonBody)
                                send(payload)
                            } catch (e: Exception) {
                                Log.e("WebSocket", "Failed to parse payload: ${e.message}")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("WebSocket", "Websocket connection error: ${e.message}")
        }
    }
}