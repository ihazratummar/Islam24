package com.hazrat.remote.api.profile

import com.hazrat.remote.dto.LiveCommunityTickerPayload
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 08/08/26
 */

interface SupporterWebSocketApi {

    fun listenToCommunityUpdates() : Flow<LiveCommunityTickerPayload>

}