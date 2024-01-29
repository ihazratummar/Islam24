package com.hazrat.islam24.domain.usecases.app_entry

import com.hazrat.islam24.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadAppEntry @Inject constructor(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Boolean>{
        return localUserManager.readAppEntry()
    }
}