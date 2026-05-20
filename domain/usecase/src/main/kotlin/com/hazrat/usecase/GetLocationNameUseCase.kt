package com.hazrat.usecase

import com.hazrat.database.entity.LocationDetailsEntity
import com.hazrat.domain.repository.LocationNameRepository
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 15/05/26
 */

class GetLocationNameUseCase(
    private val locationNameRepository: LocationNameRepository
) {

    operator fun invoke(): Flow<LocationDetailsEntity> {
        return locationNameRepository.observeLocationInfo()
    }

}