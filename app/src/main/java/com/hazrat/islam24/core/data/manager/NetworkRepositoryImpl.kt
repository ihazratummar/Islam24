package com.hazrat.islam24.core.data.manager

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.util.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class NetworkRepositoryImpl @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : NetworkRepository {


    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    override val networkStatus: StateFlow<ConnectivityObserver.Status> = _networkStatus


    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun observeNetworkStatus() {
        connectivityObserver.observer().onEach { status ->
            Log.d("NetworkRepositoryImpl", "Observed network status: $status")
            _networkStatus.value = status
        }.launchIn(repositoryScope)
    }
}