package com.hazrat.zakat.domain.usecase

import com.hazrat.database.entity.zakat.ZakatEntity
import com.hazrat.zakat.domain.repository.ZakatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 19-06-2025
 */

class GetZakatDetailsUseCase @Inject constructor(
    private val zakatRepository: ZakatRepository
) {

    operator fun invoke(id: String) : Flow<ZakatEntity> {
        return zakatRepository.getZakatDetails(id)
    }

}