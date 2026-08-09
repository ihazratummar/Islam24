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
import kotlinx.coroutines.flow.onEach


import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.AppError


/**
 * @author hazratummar
 * Created on 07/08/26
 */

class ProfileRepositoryImpl(
    private val dao: UserDao,
    private val webSocketApi: SupporterWebSocketApi,
    private val profileApi: ProfileApi,
    private val userSupportStatusDao: UserSupportStatusDao,
    private val supporterTickerDao: com.hazrat.database.dao.SupporterTickerDao
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
        return webSocketApi.listenToCommunityUpdates().onEach { dto ->
            try {
                supporterTickerDao.insertTicker(dto.toEntity())
                supporterTickerDao.trimOldTickers()
            } catch (e: Exception) {
                Log.e("ProfileRepoImpl", "Error saving ticker to DB: ${e.message}")
            }
        }.map { it.toModel() }
    }

    override fun getRecentTickers(): Flow<List<SupporterTickerModel>> {
        return supporterTickerDao.getRecentTickers().map { list ->
            list.map { it.toModel() }
        }
    }

    override suspend fun saveTicker(ticker: SupporterTickerModel) {
        try {
            supporterTickerDao.insertTicker(ticker.toEntity())
            supporterTickerDao.trimOldTickers()
        } catch (e: Exception) {
            Log.e("ProfileRepoImpl", "Error saving ticker to DB: ${e.message}")
        }
    }

    override suspend fun insertSupporterStatus(): Result<Unit, AppError> {
        return try {
            val data = profileApi.getSupportStatus()
            if (data != null) {
                userSupportStatusDao.insertSupporterStatus(data.toEntity())
                Result.Success(Unit)
            } else {
                Log.e("profileImpl", "Error Loading User Support Status $data")
                Result.Error(AppError.Custom("Support status response was empty"))
            }
        } catch (e: Exception) {
            Log.e("profileImpl", "Error Loading User Support Status ${e.message}")
            Result.Error(AppError.ExceptionCaught(e.message ?: "Unknown exception"))
        }
    }

    override fun getSupporterStatus(): Flow<SupporterStatusModel?> {
        return userSupportStatusDao.getSupportDetails().map { it?.toModel() }
    }

    override suspend fun clearLocalStatus() {
        userSupportStatusDao.deleteUserStatus()
    }
}