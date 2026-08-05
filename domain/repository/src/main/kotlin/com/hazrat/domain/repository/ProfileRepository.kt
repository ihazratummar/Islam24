package com.hazrat.domain.repository

import com.hazrat.database.entity.profile.UserEntity
import com.hazrat.model.profile.SupporterStatusModel
import com.hazrat.model.profile.SupporterTickerModel
import com.hazrat.model.profile.UserModel
import kotlinx.coroutines.flow.Flow

/**
 * Clean Domain Repository interface for User Profile & Support status.
 * @author Hazrat Ummar Shaikh
 */
interface ProfileRepository {
    fun getProfile(): Flow<UserModel?>

    suspend fun insertUser(user: UserEntity)
    suspend fun deleteLocalProfile()

    fun listenToSupporterUpdate(): Flow<SupporterTickerModel>

    suspend fun insertSupporterStatus()

    fun getSupporterStatus(): Flow<SupporterStatusModel?>

    suspend fun clearLocalStatus()
}