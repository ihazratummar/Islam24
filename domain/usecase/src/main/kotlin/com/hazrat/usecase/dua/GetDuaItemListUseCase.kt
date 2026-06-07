package com.hazrat.usecase.dua

import com.hazrat.domain.repository.DuaRepository
import com.hazrat.model.DuaItemModel
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 30/05/26
 */

class GetDuaItemListUseCase (
    private val duaRepository: DuaRepository
) {

    operator fun invoke(categoryId: Int) : Flow<Result<List<DuaItemModel>, DatabaseError>> {
        return duaRepository.getDuaItems(categoryId = categoryId)
    }

}