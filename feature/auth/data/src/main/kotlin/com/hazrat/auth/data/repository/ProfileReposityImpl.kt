package com.hazrat.auth.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.auth.domain.repository.SyncRepository
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.AuthState
import com.hazrat.model.UserData
import com.hazrat.utils.Constants.INTERNALSTORAGEPICTUREFOLDER
import com.hazrat.utils.Constants.PROFILE_PICTURE
import com.hazrat.utils.network.ConnectivityObserver
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError
import com.hazrat.utils.result.error.UserDataError
import com.hazrat.utils.result.error.UserDataSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

/**
 * @author Hazrat Ummar Shaikh
 */

class ProfileRepositoryImpl(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val syncRepository: SyncRepository,
    private val connectivityObserver: ConnectivityObserver
) : ProfileRepository {

    override val authState: Flow<AuthState> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                trySend(AuthState.Authenticated)
            } else {
                trySend(AuthState.Unauthenticated)
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override val currentUser: Flow<UserData?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                trySend(null)
            } else {
                // Fetch UserData from Firestore when auth state changes
                // This might be better handled by a separate Flow if we want real-time updates from Firestore too
                fireStore.collection("user").document(firebaseUser.uid)
                    .addSnapshotListener { snapshot, _ ->
                        val userData = snapshot?.toObject<UserData>()
                        trySend(userData)
                    }
            }
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }

    override suspend fun login(email: String, password: String): Result<Unit, AuthError> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser ?: return Result.Error(AuthError.USER_NOT_FOUND)

            // Perform sync
            syncRepository.syncDataOnLogin()

            // Handle profile picture
            val imageRef = storage.reference.child("image/${user.uid}/profile_image")
            try {
                val uri = imageRef.downloadUrl.await()
                saveProfilePictureLocally(uri)
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Failed to download profile picture on login", e)
            }

            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.Error(mapFirebaseError(e))
        } catch (e: Exception) {
            Result.Error(AuthError.UNKNOWN_ERROR)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Result<Unit, AuthError> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.Error(AuthError.UNKNOWN_ERROR)

            val userData = UserData(
                userId = user.uid,
                fullName = name,
                email = email
            )
            fireStore.collection("user").document(user.uid).set(userData).await()
            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.Error(mapFirebaseError(e))
        } catch (e: Exception) {
            Result.Error(AuthError.UNKNOWN_ERROR)
        }
    }

    override suspend fun signOut(): Result<Unit, AuthError> {
        return try {
            auth.signOut()
            deleteProfilePictureLocally()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AuthError.UNKNOWN_ERROR)
        }
    }

    override suspend fun fetchUserData(): Result<UserData, UserDataError> {
        val userId = auth.currentUser?.uid ?: return Result.Error(UserDataError.INVALID_USER_ID)
        return try {
            val document = fireStore.collection("user").document(userId).get().await()
            val user = document.toObject<UserData>()
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(UserDataError.INVALID_USER_ID)
            }
        } catch (e: Exception) {
            Result.Error(UserDataError.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateName(userData: UserData): Result<UserDataSuccess, UserDataError> {
        if (connectivityObserver.observer().first() != ConnectivityObserver.Status.Available) {
            return Result.Error(UserDataError.NO_INTERNET)
        }

        val userId = auth.currentUser?.uid ?: return Result.Error(UserDataError.INVALID_USER_ID)
        return try {
            fireStore.collection("user").document(userId)
                .update("fullName", userData.fullName)
                .await()
            Result.Success(UserDataSuccess.SUCCESS_NAME_UPDATE)
        } catch (e: Exception) {
            Result.Error(UserDataError.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateBio(userData: UserData): Result<UserDataSuccess, UserDataError> {
        if (connectivityObserver.observer().first() != ConnectivityObserver.Status.Available) {
            return Result.Error(UserDataError.NO_INTERNET)
        }

        val userId = auth.currentUser?.uid ?: return Result.Error(UserDataError.INVALID_USER_ID)
        return try {
            fireStore.collection("user").document(userId)
                .update("bio", userData.bio)
                .await()
            Result.Success(UserDataSuccess.SUCCESS_BIO_UPDATE)
        } catch (e: Exception) {
            Result.Error(UserDataError.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateProfilePicture(uri: Uri): Result<Unit, UserDataError> {
        val userId = auth.currentUser?.uid ?: return Result.Error(UserDataError.INVALID_USER_ID)
        return try {
            val compressedUri = withContext(Dispatchers.IO) {
                compressImage(context, uri) ?: uri
            }
            val imageRef = storage.reference.child("image/$userId/profile_image")
            imageRef.putFile(compressedUri).await()
            val downloadUrl = imageRef.downloadUrl.await()

            fireStore.collection("user").document(userId)
                .update("profilePictureUrl", downloadUrl.toString())
                .await()

            saveProfilePictureLocally(downloadUrl)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(UserDataError.UNKNOWN_ERROR)
        }
    }

    override fun saveProfilePictureLocally(uri: Uri) {
        val directory = createDirectory()
        val file = File(directory, "profile_picture.jpg")

        if (file.exists()) {
            file.delete()
        }

        try {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString())
            val localFile = File.createTempFile(PROFILE_PICTURE, "jpg", context.cacheDir)

            storageRef.getFile(localFile).addOnSuccessListener {
                localFile.copyTo(file, overwrite = true)
            }
        } catch (e: Exception) {
            Log.e("ProfilePicture", "Error saving profile picture", e)
        }
    }

    override fun deleteProfilePictureLocally() {
        val directory = createDirectory()
        val file = File(directory, "profile_picture.jpg")
        if (file.exists()) {
            file.delete()
        }
    }

    private fun createDirectory(): File {
        val file = File(context.filesDir, INTERNALSTORAGEPICTUREFOLDER)
        if (!file.exists()) file.mkdirs()
        return file
    }

    private fun compressImage(context: Context, uri: Uri): Uri? {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val file = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
            file.outputStream().use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }

    private fun mapFirebaseError(e: FirebaseAuthException): AuthError {
        return when (e.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> AuthError.EMAIL_ALREADY_IN_USE
            "ERROR_WRONG_PASSWORD" -> AuthError.WRONG_PASSWORD
            "ERROR_USER_NOT_FOUND" -> AuthError.USER_NOT_FOUND
            "ERROR_WEAK_PASSWORD" -> AuthError.WEAK_PASSWORD
            "ERROR_INVALID_EMAIL" -> AuthError.INVALID_CREDENTIALS
            else -> AuthError.UNKNOWN_ERROR
        }
    }
}