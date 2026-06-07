package com.hazrat.remote.api


import com.hazrat.remote.dto.AllahNamesDto
import retrofit2.http.GET


/**
 * Interface representing the API service for retrieving the names of Allah.
 * This interface defines methods for fetching the names of Allah from an external API.
 */

interface NamesApi {

    /**
     * Retrieves the names of Allah from the API.
     *
     * @return AllahNamesDto containing the names of Allah.
     */
    @GET("99_Names_Of_Allah.json")
    suspend fun getAllNames(): AllahNamesDto
}