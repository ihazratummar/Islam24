package com.hazrat.auth.data.repository

import android.util.Log
import com.hazrat.auth.domain.repository.SyncRepository
import com.hazrat.domain.repository.QiblaRepository
import com.hazrat.domain.repository.QuranRepository
import com.hazrat.zakat.domain.repository.ZakatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-01-2025
 */

class SyncRepositoryImpl(
    private val zakatRepository: ZakatRepository,
    private val quranRepository: QuranRepository,
    private val qiblaRepository: QiblaRepository
): SyncRepository {

    override suspend fun syncDataOnLogin() : Long{
        return coroutineScope {
            val zakatTime = async(Dispatchers.IO) { measureTimeMillis { syncZakatData() } }
            val quranTime = async(Dispatchers.IO) { measureTimeMillis { syncQuranData() } }
            val compassTime = async(Dispatchers.IO) { measureTimeMillis { syncCompassData() } }

            val totalTime = zakatTime.await() + quranTime.await() + compassTime.await()
            Log.d("SyncRepository", "Total sync time: $totalTime ms")
            totalTime
        }
    }

    private suspend fun syncZakatData() {
        withContext(SupervisorJob()) {
            zakatRepository.syncData()
        }
    }

    private suspend fun syncQuranData() {
        withContext(SupervisorJob()) {
            quranRepository.syncQuranDataOnLogin()
        }
    }

    private suspend fun syncCompassData() {
        withContext(SupervisorJob()) {
            qiblaRepository.syncCompassDataIfLoggedIn()
        }
    }
}