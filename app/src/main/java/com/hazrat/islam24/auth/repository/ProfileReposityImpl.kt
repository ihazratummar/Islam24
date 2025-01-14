package com.hazrat.islam24.auth.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.model.UserData
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.islam24.auth.presentation.profiledetails.ProfileAction
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.Constants.INTERNALSTORAGEPICTUREFOLDER
import com.hazrat.islam24.util.Constants.PROFILE_PICTURE
import com.hazrat.islam24.util.error.Result
import com.hazrat.islam24.util.error.UserDataError
import com.hazrat.islam24.util.error.UserDataSuccess
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class ProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val networkRepository: NetworkRepository,
) : ProfileRepository {

    private val _authState = MutableLiveData<AuthState>()
    override val authState: LiveData<AuthState> = _authState

    private val _profileState = MutableStateFlow(ProfileState())
    override val profileState = _profileState.asStateFlow()

    private val networkStatus: StateFlow<ConnectivityObserver.Status> = networkRepository.networkStatus


    private val _profileActionState = MutableLiveData<ProfileAction>()
    override val profileActionState: LiveData<ProfileAction> = _profileActionState

    override fun inviteFriend() {
        val text = context.getString(R.string.invite_friend)
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val shareIntent = Intent.createChooser(intent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(shareIntent)
    }


    override fun rateUs(activity: Activity) {
        val reviewManager = ReviewManagerFactory.create(context)
        val request = reviewManager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewManager.launchReviewFlow(activity, reviewInfo)
                    .addOnCompleteListener { launchTask ->
                        Log.d("ProfileRepositoryImpl", "rateUs: ${launchTask.result}")
                        if (launchTask.exception == null) {
                            _profileState.update {
                                it.copy(
                                    isRatingDialogOpen = true
                                )
                            }
                        }
                    }
            }
        }
    }

    override fun openRatingDialog() {
        _profileState.update {
            it.copy(
                isRatingDialogOpen = !it.isRatingDialogOpen
            )
        }
    }

    override fun goToRate() {
        val intent: Intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                "https://play.google.com/store/apps/details?id=${context.packageName}"
            )
            setPackage("com.android.vending")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
        _profileState.update {
            it.copy(
                isRatingDialogOpen = false
            )
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
                        _profileState.update {
                            it.copy(
                                userData = it.userData?.copy(
                                    profilePictureUrl = uri.toString()
                                )
                            )
                        }
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("ImageCompression", "Failed to compress image: ${e.message}")
            null
        }
    }

    override fun fetchUserData() {
        _authState.value = AuthState.Loading
        val userId = auth.currentUser?.uid ?: return
        fireStore.collection("user").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    _profileState.update {
                        it.copy(
                            userData = document.toObject<UserData>()!!
                        )
                    }
                } else {
                    _profileState.update {
                        it.copy(
                            userData = UserData(fullName = "", email = "")
                        )
                    }
                }
                _authState.value = AuthState.Authenticated
            }.addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
    }

    override fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
            fetchUserData()

        }
    }

    override fun clickNameUpdateDialog() {
        _profileState.update {
            it.copy(
                isNameDialogOpen = !it.isNameDialogOpen
            )
        }
    }

    override suspend fun updateName(userData: UserData): Result<UserDataSuccess, UserDataError> {
        return try {
            if (networkStatus.value == ConnectivityObserver.Status.Available) {
                val userId =
                    auth.currentUser?.uid
                        ?: return Result.Error(UserDataError.INVALID_USER_ID)

                // Using await() to handle the Firestore update asynchronously
                fireStore.collection("user").document(userId)
                    .update("fullName", userData.fullName)
                    .await()

                // Update local state
                _profileState.update {
                    it.copy(
                        userData = userData.copy(
                            fullName = userData.fullName,
                            email = _profileState.value.userData?.email,
                            bio = _profileState.value.userData?.bio
                        )
                    )
                }

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
        return try {
            if (networkStatus.value == ConnectivityObserver.Status.Available) {
                val userId =
                    auth.currentUser?.uid
                        ?: return Result.Error(UserDataError.INVALID_USER_ID)
                fireStore.collection("user").document(userId)
                    .update(
                        mapOf(
                            "bio" to userData.bio,
                        )
                    ).await()
                _profileState.update {
                    it.copy(
                        userData = userData.copy(
                            bio = userData.bio,
                            fullName = _profileState.value.userData?.fullName,
                            email = _profileState.value.userData?.email,
                            profilePictureUrl = _profileState.value.userData?.profilePictureUrl
                        )
                    )
                }
                return Result.Success(data = UserDataSuccess.SUCCESS_BIO_UPDATE)
            } else {
                delay(1000L)
                return Result.Error(UserDataError.NO_INTERNET)
            }
        } catch (e: Exception) {
            return Result.Error(UserDataError.UNKNOWN_ERROR)
        }
    }


    override fun updateNameValue(name: String) {
        _profileState.update {
            it.copy(
                userData = UserData(
                    fullName = name,
                    email = _profileState.value.userData?.email,
                    bio = _profileState.value.userData?.bio
                )
            )
        }
    }

    override fun clickBioUpdateDialog() {
        _profileState.update {
            it.copy(
                isBioDialogOpen = !it.isBioDialogOpen
            )
        }
    }

    override fun updateBioValue(bio: String) {
        _profileState.update {
            it.copy(
                userData = UserData(
                    bio = bio,
                    email = _profileState.value.userData?.email,
                    fullName = _profileState.value.userData?.fullName
                )
            )
        }
    }

    override suspend fun signOut() {
        if (networkStatus.value == ConnectivityObserver.Status.Available) {
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

    override fun refreshProfile() {
        _profileActionState.value = ProfileAction.Idle
    }

}