package com.hazrat.auth.data.repository

import android.util.Log
import com.hazrat.auth.data.mapper.toEntity
import com.hazrat.auth.data.mapper.toModel
import com.hazrat.database.dao.UserDao
import com.hazrat.database.dao.UserSupportStatusDao
import com.hazrat.database.entity.profile.UserEntity
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.profile.SupporterStatusModel
import com.hazrat.model.profile.SupporterTickerModel
import com.hazrat.model.profile.UserModel
import com.hazrat.remote.api.profile.ProfileApi
import com.hazrat.remote.api.profile.SupporterWebSocketApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest


/**
 * @author hazratummar
 * Created on 07/08/26
 */

class ProfileRepositoryImpl(
    private val dao: UserDao,
    private val webSocketApi: SupporterWebSocketApi,
    private val profileApi: ProfileApi,
    private val userSupportStatusDao: UserSupportStatusDao
) : ProfileRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProfile(): Flow<UserModel?> {
        return dao.getUser().mapLatest { it?.toModel() }
    }

    override suspend fun insertUser(user: UserEntity) {
        dao.insertUser(user)
    }

    override suspend fun deleteLocalProfile() {
        dao.deleteUser()
    }

    override fun listenToSupporterUpdate(): Flow<SupporterTickerModel> {
        return webSocketApi.listenToCommunityUpdates().map { it.toModel() }
    }

    override suspend fun insertSupporterStatus() {
        try {
            val data = profileApi.getSupportStatus()
            if (data != null) {
                userSupportStatusDao.insertSupporterStatus(data.toEntity())
            }else{
                Log.e("profileImpl", "Error Loading User Support Status $data")
            }
        } catch (e: Exception) {
            Log.e("profileImpl", "Error Loading User Support Status ${e.message}")
        }
    }

    override fun getSupporterStatus(): Flow<SupporterStatusModel?> {
        return userSupportStatusDao.getSupportDetails().map { it?.toModel() }
    }

    override suspend fun clearLocalStatus() {
        userSupportStatusDao.deleteUserStatus()
    }
}