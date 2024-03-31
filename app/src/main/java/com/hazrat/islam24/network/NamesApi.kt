package com.hazrat.islam24.network


import com.hazrat.islam24.domain.model.namesofallah.NamesDataModel
import retrofit2.http.GET
import javax.inject.Singleton


/**
 * Interface representing the API service for retrieving the names of Allah.
 * This interface defines methods for fetching the names of Allah from an external API.
 */
@Singleton
interface NamesApi {

    /**
     * Retrieves the names of Allah from the API.
     *
     * @return NamesDataModel containing the names of Allah.
     */
    @GET("99_Names_Of_Allah.json")
    suspend fun getAllNames(): NamesDataModel
}