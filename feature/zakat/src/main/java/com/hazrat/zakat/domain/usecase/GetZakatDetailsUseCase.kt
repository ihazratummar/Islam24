package com.hazrat.zakat.domain.usecase

import com.hazrat.database.entity.zakat.ZakatEntity
import com.hazrat.zakat.domain.repository.ZakatRepository
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 19-06-2025
 */

class GetZakatDetailsUseCase(
    private val zakatRepository: ZakatRepository
) {

    operator fun invoke(id: String) : Flow<ZakatEntity> {
        return zakatRepository.getZakatDetails(id)
    }

}