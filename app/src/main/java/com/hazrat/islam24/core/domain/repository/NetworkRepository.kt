package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.util.ConnectivityObserver
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Hazrat Ummar Shaikh
 */

interface NetworkRepository {


    val networkStatus: StateFlow<ConnectivityObserver.Status>
    fun observeNetworkStatus()

}