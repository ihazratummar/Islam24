package com.hazrat.islam24.auth.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.model.UserData
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
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
    private val connectivityObserver: ConnectivityObserver
) : ProfileRepository {

    private val _authState = MutableLiveData<AuthState>()
    override val authState: LiveData<AuthState> = _authState

    private val _profileState = MutableStateFlow(ProfileState())
    override val profileState = _profileState.asStateFlow()


    private val _networkStatus = mutableStateOf(ConnectivityObserver.Status.Unavailable)

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

    override fun rateUs() {
        TODO("Not yet implemented")
    }

    override fun updateProfilePicture(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("image/$userId/$uuid")
        val uploadTask = imageRef.putFile(uri)
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

    override fun updateName(name: UserData) {
        val userId = auth.currentUser?.uid ?: return
        fireStore.collection("user").document(userId)
            .update(
                mapOf(
                    "fullName" to name.fullName,
                )
            )
            .addOnSuccessListener {
                _profileState.update {
                    it.copy(
                        userData = name.copy(
                            fullName = name.fullName,
                        )
                    )
                }
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message.toString())
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

    override fun updateNameValue(name: String) {
        _profileState.update {
            it.copy(
                userData = UserData(
                    fullName = name
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
                    bio = bio
                )
            )
        }
    }

    override fun updateBio(bio: UserData) {
        if (_networkStatus.value == ConnectivityObserver.Status.Available){
            val userId = auth.currentUser?.uid ?: return
            fireStore.collection("user").document(userId)
                .update(
                    mapOf(
                        "bio" to bio.bio,
                    )
                )
                .addOnSuccessListener {
                    _profileState.update {
                        it.copy(
                            userData = bio.copy(
                                bio = bio.bio,
                                fullName = bio.fullName
                            )
                        )
                    }
                }
                .addOnFailureListener { e ->
                    _authState.value = AuthState.Error(e.message.toString())
                }
        }else{
            _authState.value = AuthState.Error("Check Internet Connection")
            Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    override fun refreshProfilePicture() {
        _authState.value = AuthState.Loading
        val userId = auth.currentUser?.uid ?: return
        fireStore.collection("user").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Update profileState with the latest user data
                    val userData = document.toObject<UserData>()!!
                    _profileState.update {
                        it.copy(
                            userData = it.userData?.copy(
                                profilePictureUrl = userData.profilePictureUrl
                            )
                        )
                    }
                } else {
                    // Handle the case where the document doesn't exist
                    _profileState.update {
                        it.copy(
                            userData = it.userData?.copy(
                                profilePictureUrl = ""
                            )
                        )
                    }
                }
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
    }

    override fun refreshName() {
        _authState.value = AuthState.Loading
        val userId = auth.currentUser?.uid ?: return
        fireStore.collection("user").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Update profileState with the latest user data
                    val userData = document.toObject<UserData>()!!
                    _profileState.update {
                        it.copy(
                            userData = it.userData?.copy(
                                fullName = userData.fullName,
                                email = userData.email
                            )
                        )
                    }
                } else {
                    // Handle the case where the document doesn't exist
                    _profileState.update {
                        it.copy(
                            userData = it.userData?.copy(
                                fullName = ""
                            )
                        )
                    }
                }
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
    }

    override fun refreshBio() {
        _authState.value = AuthState.Loading
        val userId = auth.currentUser?.uid ?: return
        fireStore.collection("user").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Update profileState with the latest user data
                    val userData = document.toObject<UserData>()!!
                    _profileState.update {
                        it.copy(
                            userData = it.userData?.copy(
                                bio = userData.bio,
                                email = userData.email
                            )
                        )
                    }
                } else {
                    // Handle the case where the document doesn't exist
                    _profileState.update {
                        it.copy(
                            userData = it.userData?.copy(
                                bio = ""
                            )
                        )
                    }
                }
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
    }

    override fun signOut() {
        if (_networkStatus.value == ConnectivityObserver.Status.Available) {
            auth.signOut()
            _authState.value = AuthState.Unauthenticated
            Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show()
            _authState.value = AuthState.Error("Check Internet Connection")
        }
    }

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override suspend fun networkObserver() {
        connectivityObserver.observer().onEach { status ->
            _networkStatus.value = status
            Log.d("NetworkStatus", "Network Status: ${status.name}")
        }.launchIn(repositoryScope)
    }

}