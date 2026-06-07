package com.hazrat.usecase.dua

import com.hazrat.domain.repository.DuaRepository
import com.hazrat.model.DuaCategoryModel
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 30/05/26
 */

class SearchAndGetDuaCategoriesUseCase(
    private val duaRepository: DuaRepository
) {

    operator fun invoke(query: String) : Flow<Result<List<DuaCategoryModel>, DatabaseError>> {
        return duaRepository.searchDuaCategory(query = query)
    }

}