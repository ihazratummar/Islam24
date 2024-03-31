package com.hazrat.islam24.domain.manager

import kotlinx.coroutines.flow.Flow

/**
 * Interface defining methods for managing user preferences locally.
 * This interface provides functionality to save and read the app entry status.
 */
interface LocalUserManager {

    /**
     * Saves the app entry status locally.
     */
    suspend fun saveAppEntry()

    /**
     * Reads the app entry status locally.
     *
     * @return Flow emitting a Boolean value indicating whether the app entry status is set.
     */
    fun readAppEntry(): Flow<Boolean>
}