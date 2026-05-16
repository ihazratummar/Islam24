package com.hazrat.usecase

import com.hazrat.domain.repository.LocationNameRepository
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 15/05/26
 */

class GetLocationNameUseCase(
    private val locationNameRepository: LocationNameRepository
) {

    suspend operator fun invoke(): Flow<String> {
        return locationNameRepository.locationName()
    }

}