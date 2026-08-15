package com.hazrat.usecase.profile

import android.content.Context
import com.hazrat.domain.repository.AppSyncScheduler
import com.hazrat.domain.repository.SyncRepository


/**
 * @author hazratummar
 * Created on 14/08/26
 */

class SyncDataUseCase(
    private val appSyncScheduler: AppSyncScheduler
) {


    operator fun invoke(){
        appSyncScheduler.scheduleImmediateSync()
    }

}