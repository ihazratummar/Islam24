package com.hazrat.athkar.repository

import com.hazrat.athkar.mapper.toCategoryList
import com.hazrat.athkar.mapper.toItemListModel
import com.hazrat.database.dao.DuaDao
import com.hazrat.domain.repository.DuaRepository
import com.hazrat.model.DuaCategoryModel
import com.hazrat.model.DuaItemModel
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest


/**
 * @author hazratummar
 * Created on 30/05/26
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DuaRepositoryImpl(
    private val duaDao: DuaDao
) : DuaRepository {


    override fun getDuaCategory():  Flow<Result<List<DuaCategoryModel>, DatabaseError>> {
        return duaDao.getDuaCategory()
            .mapLatest {
                Result.Success(it.toCategoryList())
            }.catch {
                Result.Error(DatabaseError.NotFound)
            }
    }

    override fun getDuaItems(categoryId: Int): Flow<Result<List<DuaItemModel>, DatabaseError>> {
        return duaDao.getDua(categoryId = categoryId)
            .mapLatest {
                Result.Success(it.toItemListModel())
            }.catch {
                Result.Error(DatabaseError.NotFound)
            }
    }

    override fun searchDuaCategory(query: String): Flow<Result<List<DuaCategoryModel>, DatabaseError>> {
        return duaDao.searchDuaCategory(title = query)
            .mapLatest {
                Result.Success(it.toCategoryList())
            }.catch {
                Result.Error(DatabaseError.NotFound)
            }
    }
}