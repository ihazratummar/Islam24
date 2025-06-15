package com.hazrat.usecase

import com.hazrat.datastore.AppDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 15-06-2025
 */

class DarkModeUseCase @Inject constructor(
    private val appDataStore: AppDataStore
) {

    operator fun invoke(): Flow<Boolean> {
        return appDataStore.isDarkModeEnabled
    }

}