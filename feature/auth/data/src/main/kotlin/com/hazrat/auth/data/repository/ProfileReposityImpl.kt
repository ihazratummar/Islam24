package com.hazrat.auth.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
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
import com.hazrat.utils.result.error.UserDataError
import com.hazrat.utils.result.error.UserDataSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
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
    private val coroutineScope: CoroutineScope,
    private val connectivityObserver: ConnectivityObserver
) : ProfileRepository {

    private val _authState = MutableLiveData<AuthState>()
    override val authState: LiveData<AuthState> = _authState


    override suspend fun login(email: String, password: String): Boolean {
        _authState.value = AuthState.Loading

        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user == null) {
                            _authState.value = AuthState.Unauthenticated
                            continuation.resume(false) { _, _, _ -> }
                            return@addOnCompleteListener
                        }

                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                fetchUserData()
                                val syncTime = syncRepository.syncDataOnLogin()
                                delay(syncTime)
                                val imageRef = storage.reference.child("image/${user.uid}/profile_image")
                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                    saveProfilePictureLocally(uri)
                                }
                                _authState.postValue(AuthState.Authenticated)
                                continuation.resume(true) { _, _, _ -> }
                            } catch (e: Exception) {
                                _authState.value = AuthState.Error(e.message ?: "Login failed")
                                continuation.resume(false) { _, _, _ -> }
                            }
                        }
                    } else {
                        _authState.value = AuthState.Unauthenticated
                        continuation.resume(false) { _, _, _ -> }
                    }
                }
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        _authState.value = AuthState.Loading

        return suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user == null) {
                            _authState.value = AuthState.Unauthenticated
                            continuation.resume(false) { cause, _, _ -> }
                            return@addOnCompleteListener
                        }

                        val userId = user.uid
                        val userData = UserData(
                            userId = userId,
                            fullName = name,
                            email = email
                        )
                        fireStore.collection("user").document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Authenticated
                                continuation.resume(true) { cause, _, _ -> }
                            }
                            .addOnFailureListener { e ->
                                _authState.value = AuthState.Error(e.message.toString())
                                continuation.resume(false) { cause, _, _ -> }
                            }
                    } else {
                        _authState.value = AuthState.Unauthenticated
                        continuation.resume(false) { cause, _, _ -> }
                    }
                }
        }
    }


    override fun updateProfilePicture(uri: Uri) {
        val compressUri = compressImage(context, uri) ?: uri
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference
        val imageRef = storageRef.child("image/$userId/profile_image")
        val uploadTask = imageRef.putFile(compressUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                fireStore.collection("user").document(userId)
                    .update(
                        mapOf("profilePictureUrl" to uri.toString())
                    ).addOnSuccessListener {
                        saveProfilePictureLocally(uri = uri)
                    }
            }
        }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message.toString())
            }
    }

    override fun saveProfilePictureLocally(uri: Uri) {
        val directory = context.createDirectory()

        // Define the file name as a static name ("profile_picture.jpg")
        val fileName = "profile_picture.jpg"
        val file = File(directory, fileName)

        // If the file already exists, delete it before saving the new one
        if (file.exists()) {
            val deleted = file.delete()  // Delete the old file before saving the new one
            if (!deleted) {
                Log.e("ProfilePicture", "Failed to delete existing file.")
                return
            }
        }

        try {
            // Download the image file from Firebase Storage using the URL provided
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString())

            // Use getFile() to download the file to local storage
            val localFile = File.createTempFile(PROFILE_PICTURE, "jpg", context.cacheDir) // Create a temp file in cache

            storageRef.getFile(localFile).addOnSuccessListener {
                // After download is complete, copy the downloaded file to the desired location
                localFile.copyTo(file, overwrite = true)
                Log.d("ProfilePicture", "Profile picture saved successfully to ${file.absolutePath}")
            }.addOnFailureListener { e ->
                Log.e("ProfilePicture", "Failed to download image from Firebase", e)
            }
        } catch (e: Exception) {
            Log.e("ProfilePicture", "Error saving profile picture", e)
        }
    }

    fun deleteProfilePictureLocally() {
        val directory = context.createDirectory() // Ensure this points to the same directory
        val fileName = "profile_picture.jpg"
        val file = File(directory, fileName)

        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                Log.d("ProfilePicture", "Profile picture deleted successfully from ${file.absolutePath}")
            } else {
                Log.e("ProfilePicture", "Failed to delete profile picture from ${file.absolutePath}")
            }
        } else {
            Log.w("ProfilePicture", "Profile picture file does not exist. No action taken.")
        }
    }

    fun Context.createDirectory(): File{
        val directory = filesDir
        val file  = File(directory, INTERNALSTORAGEPICTUREFOLDER)
        if (!file.exists()) file.mkdirs()
        return file
    }

    private fun compressImage(context: Context, uri: Uri): Uri? {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val file = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
            file.outputStream().use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("ImageCompression", "Failed to compress image: ${e.message}")
            null
        }
    }

    override suspend fun fetchUserData(): Result<UserData, UserDataError> {
        _authState.postValue(AuthState.Loading)
        val userId = auth.currentUser?.uid ?: return Result.Error(UserDataError.UNKNOWN_ERROR)

        return try {
            val document = fireStore
                .collection("user")
                .document(userId)
                .get()
                .await()
            if (document.exists()){
                val user = document.toObject<UserData>()
                    ?: return Result.Error(UserDataError.INVALID_USER_ID)

                _authState.postValue(AuthState.Authenticated)
                Result.Success(user)
            }else{
                _authState.postValue(AuthState.Error("User not found"))
                Result.Error(UserDataError.INVALID_USER_ID)
            }
        }catch (e: Exception){
            _authState.postValue(AuthState.Error(e.message.toString()))
            Result.Error(UserDataError.UNKNOWN_ERROR)
        }
    }

    override suspend fun checkAuthStatus() {
        Log.d("ProfileRepository", "checkAuthStatus called")
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            Log.d("ProfileRepository", "User is not authenticated")
        } else {
            _authState.value = AuthState.Authenticated
            fetchUserData()
            Log.d("ProfileRepository", "User is authenticated")
        }
    }

    override suspend fun updateName(userData: UserData): Result<UserDataSuccess, UserDataError> {
        try {
            val status = connectivityObserver.observer().first()
            if (status== ConnectivityObserver.Status.Available) {
                val userId =
                    auth.currentUser?.uid
                        ?: return Result.Error(UserDataError.INVALID_USER_ID)

                // Using await() to handle the Firestore update asynchronously
                fireStore.collection("user").document(userId)
                    .update("fullName", userData.fullName)
                    .await()

                return Result.Success(data = UserDataSuccess.SUCCESS_NAME_UPDATE)
            } else {
                delay(1000L)
                return Result.Error(UserDataError.NO_INTERNET)
            }
        } catch (e: Exception) {
            return Result.Error(UserDataError.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateBio(userData: UserData): Result<UserDataSuccess, UserDataError> {
        try {
            val status  = connectivityObserver.observer().first()
            if ( status == ConnectivityObserver.Status.Available) {

                val userId =
                    auth.currentUser?.uid
                        ?: return Result.Error(UserDataError.INVALID_USER_ID)
                fireStore.collection("user").document(userId)
                    .update(
                        mapOf(
                            "bio" to userData.bio,
                        )
                    ).await()
                return Result.Success(data = UserDataSuccess.SUCCESS_BIO_UPDATE)
            } else {
                delay(1000L)
                return Result.Error(UserDataError.NO_INTERNET)
            }
        } catch (e: Exception) {
            return Result.Error(UserDataError.UNKNOWN_ERROR)
        }
    }


    override suspend fun signOut() {
        val status = connectivityObserver.observer().first()

        if (status== ConnectivityObserver.Status.Available) {
            _authState.value = AuthState.Loading
            delay(2000)
            auth.signOut()
            _authState.value = AuthState.Unauthenticated
            deleteProfilePictureLocally()
        } else {
            _authState.value = AuthState.Loading
            delay(2000)
            _authState.value = AuthState.Error("Check Internet Connection")
            delay(2000)
            _authState.value = AuthState.Authenticated
        }
    }
}