package com.hazrat.islam24.domain.usecases.app_entry

import com.hazrat.islam24.domain.manager.LocalUserManager
import javax.inject.Inject

class SaveAppEntry @Inject constructor(
    private val localUserManager: LocalUserManager
){
    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }

}