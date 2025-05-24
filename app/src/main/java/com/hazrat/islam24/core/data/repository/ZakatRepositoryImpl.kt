package com.hazrat.islam24.core.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.hazrat.islam24.core.data.dao.ZakatDao
import com.hazrat.islam24.core.domain.model.zakat.NisabEntity
import com.hazrat.islam24.core.domain.model.zakat.ZakatEntity
import com.hazrat.islam24.core.domain.repository.ZakatRepository
import com.hazrat.islam24.util.Constants.USER_COLLECTION
import com.hazrat.islam24.util.Constants.ZAKAT_COLLECTION
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class ZakatRepositoryImpl @Inject constructor(
    private val dao: ZakatDao,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : ZakatRepository {
    override suspend fun insertNisab(nisabEntity: NisabEntity) {

        dao.insertNisab(nisabEntity)
    }

    override suspend fun deleteNisab(nisabEntity: NisabEntity) {
        dao.deleteNisab(nisabEntity)
    }

    override fun getNisab(): Flow<NisabEntity> {
        return dao.getNisab().transform { entity ->
            if (entity == null){
                insertNisab(NisabEntity(silverPrice = 0.0))
                emit(NisabEntity(silverPrice = 0.0))
            }else{
                emit(entity)
            }
        }
    }

    override suspend fun insertZakat(zakatEntity: ZakatEntity) {
        val newZakatEntity = zakatEntity.copy(id = UUID.randomUUID().toString())
        dao.insertZakatDetails(zakatEntity = newZakatEntity)

        val userId = auth.currentUser?.uid ?: return
        fireStore.collection(USER_COLLECTION).document(userId).collection(ZAKAT_COLLECTION)
            .document(newZakatEntity.id).set(zakatEntity)
            .addOnSuccessListener {
                Toast.makeText(context, "Zakat Added Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
            }
    }

    override suspend fun deleteZakat(zakatId: String) {
        dao.deleteZakatDetails(zakatId)
        val userId = auth.currentUser?.uid?:return
        fireStore.collection(USER_COLLECTION).document(userId).collection(ZAKAT_COLLECTION)
            .document(zakatId).delete()
    }

    override fun getZakatDetails(): Flow<List<ZakatEntity>> {
        return dao.getZakatDetails()
    }

    override fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>> {
        return dao.getZakatDetailsByDateDesc()
    }

    override fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>> {
        return dao.getZakatDetailsByDateAsc()
    }


    override suspend fun syncData() {
        val userId = auth.currentUser?.uid ?: return

        try {
            // Collect the Flow to get the list of ZakatEntity objects
            val localZakatEntities = dao.getZakatDetails().first()
            val firestoreZakatEntities = mutableListOf<ZakatEntity>()

            // Fetch data from Firestore
            val querySnapshot = fireStore.collection(USER_COLLECTION).document(userId).collection(ZAKAT_COLLECTION  ).get().await()

            for (document in querySnapshot.documents) {
                val zakatEntity = document.toObject(ZakatEntity::class.java)
                zakatEntity?.let { firestoreZakatEntities.add(it) }
            }

            // Backup local data to Firestore
            for (localEntity in localZakatEntities) {
                if (!firestoreZakatEntities.any { it.id == localEntity.id }) {
                    fireStore.collection(USER_COLLECTION).document(userId).collection("zakat")
                        .document(localEntity.id).set(localEntity).await()
                }
            }

            for (firestoreEntity in firestoreZakatEntities) {
                if (!localZakatEntities.any { it.id == firestoreEntity.id }) {
                    dao.insertZakatDetails(firestoreEntity)
                }
            }
        } catch (e: Exception) {
            // Log detailed error information
            Log.e("SyncDataError", "Error synchronizing data", e)

            // Provide meaningful feedback to the user on the main thread
            withContext(Dispatchers.Main) {
                when (e) {
                    is FirebaseFirestoreException -> {
                        when (e.code) {
                            FirebaseFirestoreException.Code.UNAVAILABLE -> {
                                Toast.makeText(context, "Network error. Please check your connection and try again.", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                Toast.makeText(context, "Error synchronizing data with Firestore: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(context, "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}