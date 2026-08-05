package com.hazrat.usecase.profile

import com.hazrat.datastore.UserDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Clean Architecture UseCase to observe the user subscription state reactively.
 * @author hazratummar
 */
class IsSubscribedUseCase(
    private val userDataStore: UserDataStore
) {
    operator fun invoke(): Flow<Boolean> {
        return userDataStore.isSubscribed
    }
}
