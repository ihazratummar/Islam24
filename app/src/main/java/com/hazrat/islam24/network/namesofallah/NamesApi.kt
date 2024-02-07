package com.hazrat.islam24.network.namesofallah


import com.hazrat.islam24.domain.model.namesofallah.NamesDataModel
import retrofit2.http.GET
import javax.inject.Singleton


@Singleton
interface NamesApi {

    @GET("99_Names_Of_Allah.json")
    suspend fun getAllNames(): NamesDataModel

}