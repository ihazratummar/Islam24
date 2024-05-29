package com.hazrat.islam24.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observer(): Flow<Status>

    enum class Status{
        Available, Unavailable, Losing, Lost
    }
}