package com.hazrat.allahNames.repository

import com.hazrat.allahNames.model.namesofallah.NameOfAllahData

/**
 * @author Hazrat Ummar Shaikh
 */

interface NamesRepository {

    /**
     * Retrieves Allah's names from an external API.
     * This function fetches the names data from a remote source.
     *
     * @return A list of NameOfAllahData objects representing the names retrieved from the API.
     */
    suspend fun getAllahNamesFromApi(): List<NameOfAllahData>

    /**
     * Retrieves Allah's names from the local database.
     * This function fetches the names data stored in the local database.
     *
     * @return A list of NameEntity objects representing the names retrieved from the database.
     */
    fun getAllahNamesFromDatabase(): List<NameOfAllahData>
}