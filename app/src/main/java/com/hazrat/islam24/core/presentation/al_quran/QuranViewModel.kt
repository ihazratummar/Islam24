package com.hazrat.islam24.core.presentation.al_quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.QuranRepository
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val quranRepository: QuranRepository,
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    val quranState: StateFlow<QuranState> = quranRepository.quranState
    private val networkStatus: StateFlow<ConnectivityObserver.Status> =
        networkRepository.networkStatus
    init {
        observeNetworkStatus()
        observeQuranFromDatabase()
        viewModelScope.launch{
            quranRepository.syncQuranDataIfLoggedIn()
        }
    }

    private fun observeQuranFromDatabase() {
        viewModelScope.launch {
            quranRepository.getAllQuranData()
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkStatus.collect { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    quranRepository.downloadQuranFile()
                }
            }
        }
    }

    fun refreshQuran() {
        viewModelScope.launch {
            quranRepository.getAllQuranData()
        }
        quranRepository.loadFavoritesFromFile()
    }

    fun refreshLastRead() {
        quranRepository.refreshLastRead()
    }

    // Handle events
    fun onEvent(event: QuranEvent) {
        when (event) {
            is QuranEvent.SaveLastRead -> {
                viewModelScope.launch {
                    quranRepository.saveLastRead(
                        surahNumber = event.surahNumber,
                        ayahNumber = event.ayahNumber
                    )
                }
            }

            QuranEvent.Refresh -> {
                viewModelScope.launch {
                    quranRepository.getAllQuranData()
                }
            }

            is QuranEvent.SaveFavorite -> {
                quranRepository.toggleFavorite(
                    quranAr = event.quranAr,
                    arAyah = event.arAyah
                )
            }
        }
    }
}
