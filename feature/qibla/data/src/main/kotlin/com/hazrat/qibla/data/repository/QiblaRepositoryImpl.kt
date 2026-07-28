package com.hazrat.qibla.data.repository

import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.QiblaRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-01-2025
 */

class QiblaRepositoryImpl(
    private val userDataStore: UserDataStore
): QiblaRepository {

    override suspend fun syncCompassDataIfLoggedIn() {
        try {
            val localCompassId = userDataStore.getSelectedCompassId.first()
            Timber.tag("QiblaRepositoryImpl").d("syncCompassData local: $localCompassId")
        } catch (e: Exception) {
            Timber.tag("QiblaRepositoryImpl").d("syncCompassData: ${e.message}")
        }
    }
}