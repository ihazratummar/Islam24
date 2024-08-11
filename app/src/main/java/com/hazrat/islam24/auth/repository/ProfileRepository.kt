package com.hazrat.islam24.auth.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.model.UserData
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Hazrat Ummar Shaikh
 */

interface ProfileRepository {
    val profileState: StateFlow<ProfileState>
    val authState: LiveData<AuthState>
    fun inviteFriend()
    fun rateUs()
    fun updateProfilePicture(uri: Uri)
    fun updateName(name: UserData)
    fun fetchUserData()
    fun checkAuthStatus()
    fun clickNameUpdateDialog()
    fun updateNameValue(name: String)
    fun clickBioUpdateDialog()
    fun updateBioValue(bio: String)
    fun updateBio(bio: UserData)

    fun refreshProfilePicture()
    fun refreshName()
    fun refreshBio()

    suspend fun networkObserver()

    fun signOut()
}