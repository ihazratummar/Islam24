package com.hazrat.utils.network

import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface ConnectivityObserver {
    fun observer(): Flow<Status>

    enum class Status{
        Available, Unavailable, Losing, Lost
    }
}