package com.hazrat.islam24.core.remote.api

import com.hazrat.islam24.core.domain.model.athkar.AthkarApiModel
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton

/**
 * @author Hazrat Ummar Shaikh
 */

@Singleton
interface AthkarApiCall {

    @GET("athkar.json")
    suspend fun getAllAthkar(): Response<AthkarApiModel>

}