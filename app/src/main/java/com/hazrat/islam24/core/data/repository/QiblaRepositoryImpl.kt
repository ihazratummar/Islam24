package com.hazrat.islam24.core.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.islam24.core.domain.repository.QiblaRepository
import com.hazrat.islam24.util.Constants.USER_COLLECTION
import com.hazrat.islam24.util.datastore.UserDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-01-2025
 */

class QiblaRepositoryImpl @Inject constructor(
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
                Log.d("QiblaRepositoryImpl", "syncCompassData: $firebaseCompassId")
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
            Log.d("QiblaRepositoryImpl", "syncCompassData: ${e.message}")
        }
    }

    override suspend fun syncCompassDataOnLogIn() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        Log.d("QiblaRepositoryImpl", "Fetching data for user: $userId")
        try {
            val documentSnapshot = firebaseFirestore.collection(USER_COLLECTION).document(userId).get().await()
            if (documentSnapshot.exists()){
                val firebaseCompassId = documentSnapshot.getLong("compassModelId")?.toInt()?:1
                Log.d("QiblaRepositoryImpl", "syncCompassData Complete: $firebaseCompassId")
                userDataStore.saveSelectedCompassId(id = firebaseCompassId)
            }
        }catch (e: Exception){
            Log.d("QiblaRepositoryImpl", "syncCompassData: ${e.message}")
        }catch (e: CancellationException){
            Log.d("QiblaRepositoryImpl", "syncCompassData: ${e.message}")
        }
    }
}