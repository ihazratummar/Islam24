package com.hazrat.athkar.repository

import com.hazrat.athkar.mapper.toCategoryList
import com.hazrat.athkar.mapper.toChapterWithCountList
import com.hazrat.athkar.mapper.toItemListModel
import com.hazrat.athkar.mapper.toRecentReadDuaList
import com.hazrat.database.dao.DuaDao
import com.hazrat.database.entity.RecentDuaEntity
import com.hazrat.domain.repository.DuaRepository
import com.hazrat.model.DuaCategoryModel
import com.hazrat.model.DuaChapterWithCountModel
import com.hazrat.model.DuaItemModel
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.model.RecentReadDua
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

/**
 * @author hazratummar
 * Created on 30/05/26
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DuaRepositoryImpl(
    private val duaDao: DuaDao
) : DuaRepository {

    override fun getDuaCategory(): Flow<Result<List<DuaCategoryModel>, DatabaseError>> {
        return duaDao.getDuaCategory()
            .mapLatest {
                Result.Success(it.toCategoryList()) as Result<List<DuaCategoryModel>, DatabaseError>
            }.catch {
                emit(Result.Error(DatabaseError.NotFound))
            }.flowOn(Dispatchers.IO)
    }

    override fun getDuaItems(categoryId: Int): Flow<Result<List<DuaItemModel>, DatabaseError>> {
        return duaDao.getDua(categoryId = categoryId)
            .mapLatest {
                Result.Success(it.toItemListModel()) as Result<List<DuaItemModel>, DatabaseError>
            }.catch {
                emit(Result.Error(DatabaseError.NotFound))
            }.flowOn(Dispatchers.IO)
    }

    override fun searchDuaCategory(query: String): Flow<Result<List<DuaCategoryModel>, DatabaseError>> {
        return duaDao.searchDuaCategory(title = query)
            .mapLatest {
                Result.Success(it.toCategoryList()) as Result<List<DuaCategoryModel>, DatabaseError>
            }.catch {
                emit(Result.Error(DatabaseError.NotFound))
            }.flowOn(Dispatchers.IO)
    }

    override fun getChaptersForCategory(category: HisnulMuslimCategory): Flow<Result<List<DuaChapterWithCountModel>, DatabaseError>> {
        return duaDao.getChaptersWithDuaCount(chapterIds = category.chapterIds)
            .mapLatest {
                Result.Success(it.toChapterWithCountList()) as Result<List<DuaChapterWithCountModel>, DatabaseError>
            }.catch {
                emit(Result.Error(DatabaseError.NotFound))
            }.flowOn(Dispatchers.IO)
    }

    override fun getAllChapters(): Flow<Result<List<DuaChapterWithCountModel>, DatabaseError>> {
        return duaDao.getAllChaptersWithDuaCount()
            .mapLatest {
                Result.Success(it.toChapterWithCountList()) as Result<List<DuaChapterWithCountModel>, DatabaseError>
            }.catch {
                emit(Result.Error(DatabaseError.NotFound))
            }.flowOn(Dispatchers.IO)
    }

    override fun searchChapters(query: String): Flow<Result<List<DuaChapterWithCountModel>, DatabaseError>> {
        return duaDao.searchChaptersWithDuaCount(query = query)
            .mapLatest {
                Result.Success(it.toChapterWithCountList()) as Result<List<DuaChapterWithCountModel>, DatabaseError>
            }.catch {
                emit(Result.Error(DatabaseError.NotFound))
            }.flowOn(Dispatchers.IO)
    }

    override fun getBookmarkedDuas(): Flow<Result<List<DuaItemModel>, DatabaseError>> {
        return duaDao.getBookmarkedDuas()
            .mapLatest {
                Result.Success(it.toItemListModel()) as Result<List<DuaItemModel>, DatabaseError>
            }.catch {
                emit(Result.Error(DatabaseError.NotFound))
            }.flowOn(Dispatchers.IO)
    }

    override suspend fun toggleBookmark(duaId: Int, isBookmarked: Boolean): Result<Unit, DatabaseError> =
        withContext(Dispatchers.IO) {
            try {
                duaDao.updateBookmark(id = duaId, isBookmarked = if (isBookmarked) 1 else 0)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(DatabaseError.NotFound)
            }
        }

    override suspend fun insertRecentDua(
        chapterId: Int,
        title: String,
        duaCount: Int,
        formattedDate: String
    ) = withContext(Dispatchers.IO) {
        duaDao.insertRecentDua(
            RecentDuaEntity(
                chapterId = chapterId,
                title = title,
                duaCount = duaCount,
                formattedDate = formattedDate,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override fun getRecentDuas(): Flow<List<RecentReadDua>> {
        return duaDao.getRecentDuas().mapLatest { list ->
            list.toRecentReadDuaList()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteRecentDua(chapterId: Int) = withContext(Dispatchers.IO) {
        duaDao.deleteRecentDua(chapterId)
    }
}