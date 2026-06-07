package com.hazrat.alQuran.ui.surah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.domain.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */


class SurahViewModel(
    private val quranRepository: QuranRepository
) : ViewModel() {

    private val _surahState = MutableStateFlow(SurahState())
    val surahState: StateFlow<SurahState> = _surahState.asStateFlow()

    init {

        loadQuran()
    }



    fun loadQuran() {
        viewModelScope.launch {
            quranRepository.getAllSurahList().collectLatest { quran ->
                _surahState.update {
                    it.copy(
                        quranData = quran
                    )
                }
            }
        }
    }



    // Handle events
    fun onEvent(event: SurahEvent) {

    }
}
