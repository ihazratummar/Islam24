package com.hazrat.remote.api.sync

import com.hazrat.remote.dto.sync.SyncRequestDto
import com.hazrat.remote.dto.sync.SyncResponseDto


/**
 * @author hazratummar
 * Created on 14/08/26
 */

interface SyncApi {

    suspend fun syncData(request: SyncRequestDto) : SyncResponseDto?

}