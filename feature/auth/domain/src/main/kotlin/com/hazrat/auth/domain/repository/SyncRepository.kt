package com.hazrat.auth.domain.repository

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-01-2025
 */

interface SyncRepository {

    suspend fun syncDataOnLogin() : Long

}