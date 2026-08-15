package com.hazrat.remote.api.apiData

import android.util.Log
import com.hazrat.remote.api.sync.SyncApi
import com.hazrat.remote.dto.sync.SyncRequestDto
import com.hazrat.remote.dto.sync.SyncResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


/**
 * @author hazratummar
 * Created on 14/08/26
 */

class SyncApiImpl(
    private val httpClient: HttpClient
) : SyncApi {


    override suspend fun syncData(request: SyncRequestDto): SyncResponseDto? {
        return try {
            val response = httpClient.post("sync"){
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status.value in 200..299){
                response.body<SyncResponseDto>()
            }else{
                Log.e("SyncApiImpl", "Sync failed with ${response.status}")
                null
            }
        }catch (e: Exception){
            Log.e("SyncApiImpl", "Network exception during sync: ${e.message}")
            null
        }
    }
}