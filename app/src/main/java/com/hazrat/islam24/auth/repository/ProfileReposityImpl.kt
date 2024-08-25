package com.hazrat.islam24.auth.repository

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
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
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.error.Result
import com.hazrat.islam24.util.error.UserDataError
import com.hazrat.islam24.util.error.UserDataSuccess
import com.hazrat.islam24.util.getActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val connectivityObserver: ConnectivityObserver,
) : ProfileRepository {

    private val _authState = MutableLiveData<AuthState>()
    override val authState: LiveData<AuthState> = _authState

    private val _profileState = MutableStateFlow(ProfileState())
    override val profileState = _profileState.asStateFlow()

    private val _networkStatus = mutableStateOf(ConnectivityObserver.Status.Unavailable)


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
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("image/$userId/$uuid")
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
                    }
            }
        }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message.toString())
            }
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
            if (_networkStatus.value == ConnectivityObserver.Status.Available) {
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
            if (_networkStatus.value == ConnectivityObserver.Status.Available) {
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
        if (_networkStatus.value == ConnectivityObserver.Status.Available) {
            _authState.value = AuthState.Loading
            delay(2000)
            auth.signOut()
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Loading
            delay(2000)
            _authState.value = AuthState.Error("Check Internet Connection")
            delay(2000)
            _authState.value = AuthState.Authenticated
        }
    }

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override suspend fun networkObserver() {
        connectivityObserver.observer().onEach { status ->
            _networkStatus.value = status
            Log.d("ProfileNetworkObserver", "Network Status: $status")
        }.launchIn(repositoryScope)

    }

    override fun refreshProfile() {
        _profileActionState.value = ProfileAction.Idle
    }

}