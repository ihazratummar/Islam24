package com.hazrat.domain.repository

import com.hazrat.model.DuaCategoryModel
import com.hazrat.model.DuaChapterWithCountModel
import com.hazrat.model.DuaItemModel
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.model.RecentReadDua
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.flow.Flow

/**
 * @author hazratummar
 * Created on 30/05/26
 */
interface DuaRepository {

    fun getDuaCategory(): Flow<Result<List<DuaCategoryModel>, DatabaseError>>

    fun getDuaItems(categoryId: Int): Flow<Result<List<DuaItemModel>, DatabaseError>>

    fun searchDuaCategory(query: String): Flow<Result<List<DuaCategoryModel>, DatabaseError>>

    fun getChaptersForCategory(category: HisnulMuslimCategory): Flow<Result<List<DuaChapterWithCountModel>, DatabaseError>>

    fun getAllChapters(): Flow<Result<List<DuaChapterWithCountModel>, DatabaseError>>

    fun searchChapters(query: String): Flow<Result<List<DuaChapterWithCountModel>, DatabaseError>>

    fun getBookmarkedDuas(): Flow<Result<List<DuaItemModel>, DatabaseError>>

    suspend fun toggleBookmark(duaId: Int, isBookmarked: Boolean): Result<Unit, DatabaseError>

    suspend fun insertRecentDua(chapterId: Int, title: String, duaCount: Int, formattedDate: String)

    fun getRecentDuas(): Flow<List<RecentReadDua>>

    suspend fun deleteRecentDua(chapterId: Int)
}