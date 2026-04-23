package com.hazrat.alQuran.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.domain.repository.QuranRepository
import com.hazrat.utils.network.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */


class QuranViewModel(
    private val quranRepository: QuranRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _quranState = MutableStateFlow(QuranState())
    val quranState: StateFlow<QuranState> = _quranState.asStateFlow()

    init {
        getQuranDataFromApi()
        readQuranFile()
        observeQuranFromDatabase()
        viewModelScope.launch {
            quranRepository.syncQuranDataIfLoggedIn()
        }
    }

    private fun observeQuranFromDatabase() {
        viewModelScope.launch {
            quranRepository.getAllQuranData()
        }
    }

    private fun getQuranDataFromApi() {
        viewModelScope.launch {
            val networkState = connectivityObserver.observer().first()
            if (networkState == ConnectivityObserver.Status.Available) {
                quranRepository.downloadQuranFile()
            }
        }
    }

    fun refreshQuran() {
        viewModelScope.launch {
            quranRepository.getAllQuranData()
        }

    }

    fun readQuranFile() {
        val arFile = quranRepository.readQuranArFile()
        val bnFile = quranRepository.readQuranBnFile()
        val enFile = quranRepository.readQuranEnFile()
        val transliterationFile = quranRepository.readQuranTransliterationFile()
        Log.d("QuranViewModel", "readQuranFile: $arFile")
        _quranState.update {
            it.copy(
                arQuranData = arFile,
                quranBnData = bnFile,
                quranEnData = enFile,
                quranTransliterationData = transliterationFile
            )
        }
    }

    fun refreshLastRead() {

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
                viewModelScope.launch {
                    quranRepository.toggleFavorite(
                        surahNumber = event.quranAr.number,
                        ayahNumber = event.arAyah.number
                    )
                }
            }

            QuranEvent.AyahDropDownClick -> {
                _quranState.update {
                    it.copy(
                        isAyahDropDownOpen = !it.isAyahDropDownOpen
                    )
                }
            }

            QuranEvent.OpenQuranSettingDialog -> {
                _quranState.update {
                    it.copy(
                        isQuranSettingDiloagOpen = !it.isQuranSettingDiloagOpen
                    )
                }
            }
        }
    }
}
