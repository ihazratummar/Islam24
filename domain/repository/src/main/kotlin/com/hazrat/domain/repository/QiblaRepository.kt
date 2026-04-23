package com.hazrat.domain.repository

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-01-2025
 */

interface QiblaRepository {

    suspend fun syncCompassDataIfLoggedIn()

}