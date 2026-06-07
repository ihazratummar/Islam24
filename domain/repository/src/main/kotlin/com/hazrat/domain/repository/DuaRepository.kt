package com.hazrat.domain.repository

import com.hazrat.model.DuaCategoryModel
import com.hazrat.model.DuaItemModel
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.flow.Flow
import com.hazrat.utils.result.Result

/**
 * @author hazratummar
 * Created on 30/05/26
 */

interface DuaRepository {


    fun getDuaCategory() : Flow<Result<List<DuaCategoryModel>, DatabaseError>>

    fun getDuaItems(categoryId: Int) : Flow<Result<List<DuaItemModel>, DatabaseError>>

    fun searchDuaCategory(query: String) : Flow<Result<List<DuaCategoryModel>, DatabaseError>>

}