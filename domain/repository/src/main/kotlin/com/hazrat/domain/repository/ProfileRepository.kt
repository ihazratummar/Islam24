package com.hazrat.domain.repository

import android.net.Uri
import com.hazrat.model.AuthState
import com.hazrat.model.UserData
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AuthError
import com.hazrat.utils.result.error.UserDataError
import com.hazrat.utils.result.error.UserDataSuccess
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface ProfileRepository {

    val authState: Flow<AuthState>
    val currentUser: Flow<UserData?>

    suspend fun login(email: String, password: String): Result<Unit, AuthError>
    suspend fun signup(name: String, email: String, password: String): Result<Unit, AuthError>
    suspend fun signOut(): Result<Unit, AuthError>
    
    suspend fun fetchUserData(): Result<UserData, UserDataError>
    suspend fun updateName(userData: UserData): Result<UserDataSuccess, UserDataError>
    suspend fun updateBio(userData: UserData): Result<UserDataSuccess, UserDataError>
    
    suspend fun updateProfilePicture(uri: Uri): Result<Unit, UserDataError>
    fun saveProfilePictureLocally(uri: Uri)
    fun deleteProfilePictureLocally()
}