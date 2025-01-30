package com.hazrat.islam24.core.domain.repository

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-01-2025
 */

interface QiblaRepository {

    suspend fun syncCompassDataIfLoggedIn()

    suspend fun syncCompassDataOnLogIn()
}