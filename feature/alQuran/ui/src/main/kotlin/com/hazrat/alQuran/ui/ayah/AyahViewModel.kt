package com.hazrat.alQuran.ui.ayah

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
 * @author hazratummar
 * Created on 27/05/26
 */

class AyahViewModel(
    private val surahNumber: Int,
    private val quranRepository: QuranRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AyahState())
    val state : StateFlow<AyahState> = _state.asStateFlow()

    init {
        loadAyah()
    }


    private fun loadAyah() {
        viewModelScope.launch {
            quranRepository.getASurahAyas(surahNumber = surahNumber).collectLatest { ayahModels ->
                _state.update {
                    it.copy(
                        ayahs = ayahModels
                    )
                }
            }
        }
    }

}