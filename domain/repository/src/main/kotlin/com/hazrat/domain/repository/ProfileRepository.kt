package com.hazrat.domain.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.hazrat.model.AuthState
import com.hazrat.model.UserData
import com.hazrat.utils.result.error.UserDataError
import com.hazrat.utils.result.error.UserDataSuccess
import com.hazrat.utils.result.Result

/**
 * @author Hazrat Ummar Shaikh
 */

interface ProfileRepository {

    val authState: LiveData<AuthState>

    fun updateProfilePicture(uri: Uri)
    suspend fun login(email: String, password: String) : Boolean
    suspend fun signup(name: String, email: String, password: String, confirmPassword: String): Boolean
    suspend fun updateName(userData: UserData): Result<UserDataSuccess, UserDataError>
    suspend fun fetchUserData() :  Result<UserData, UserDataError>
    suspend fun checkAuthStatus()
    suspend fun updateBio(userData: UserData):Result<UserDataSuccess, UserDataError>
    suspend fun signOut()

    fun saveProfilePictureLocally(uri: Uri)

}