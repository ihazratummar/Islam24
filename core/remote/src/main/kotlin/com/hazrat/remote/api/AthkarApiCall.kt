package com.hazrat.remote.api

import com.hazrat.remote.dto.AthkarDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author Hazrat Ummar Shaikh
 */


interface AthkarApiCall {

    @GET("athkar.json")
    suspend fun getAllAthkar(): Response<AthkarDto>

}