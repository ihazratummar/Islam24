package com.hazrat.qibla.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.QiblaRepository
import com.hazrat.utils.Constants.USER_COLLECTION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-01-2025
 */

class QiblaRepositoryImpl(
    private val userDataStore: UserDataStore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
): QiblaRepository {

    override suspend fun syncCompassDataIfLoggedIn() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        try {
            val localCompassId = userDataStore.getSelectedCompassId.first()
            val documentSnapshot =
                firebaseFirestore.collection(USER_COLLECTION).document(userId).get().await()
            if (documentSnapshot.exists()) {
                val firebaseCompassId = documentSnapshot.getLong("compassModelId")?.toInt()
                Timber.tag("QiblaRepositoryImpl").d("syncCompassData: $firebaseCompassId")
                if (firebaseCompassId != localCompassId) {
                    userDataStore.saveSelectedCompassId(localCompassId)
                    firebaseFirestore.collection(USER_COLLECTION).document(userId)
                        .update(
                            mapOf(
                                "compassModelId" to localCompassId
                            )
                        ).await()
                }
            }
        } catch (e: Exception) {
            Timber.tag("QiblaRepositoryImpl").d("syncCompassData: ${e.message}")
        }
    }
}