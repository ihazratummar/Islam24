package com.hazrat.islam24.auth.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.model.UserData
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.islam24.auth.presentation.profiledetails.ProfileAction
import com.hazrat.islam24.util.error.Result
import com.hazrat.islam24.util.error.UserDataError
import com.hazrat.islam24.util.error.UserDataSuccess
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Hazrat Ummar Shaikh
 */

interface ProfileRepository {
    val profileState: StateFlow<ProfileState>
    val authState: LiveData<AuthState>
    val profileActionState: LiveData<ProfileAction>
    fun inviteFriend()
    fun rateUs()
    fun updateProfilePicture(uri: Uri)
    suspend fun updateName(userData: UserData): Result<UserDataSuccess, UserDataError>
    fun fetchUserData()
    fun checkAuthStatus()
    fun clickNameUpdateDialog()
    fun updateNameValue(name: String)
    fun clickBioUpdateDialog()
    fun updateBioValue(bio: String)
    suspend fun updateBio(userData: UserData):Result<UserDataSuccess, UserDataError>

    suspend fun networkObserver()

    suspend fun signOut()

    fun refreshProfile()
}