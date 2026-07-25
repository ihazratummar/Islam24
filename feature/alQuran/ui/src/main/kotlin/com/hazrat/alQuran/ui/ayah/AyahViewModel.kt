package com.hazrat.alQuran.ui.ayah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.DataStorePreference
import com.hazrat.usecase.quran.DeleteRecentSurahUseCase
import com.hazrat.usecase.quran.GetSurahAyahsUseCase
import com.hazrat.usecase.quran.SaveRecentSurahUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @author hazratummar
 * Created on 27/05/26
 */

class AyahViewModel(
    private val surahNumber: Int,
    private val getSurahAyahsUseCase: GetSurahAyahsUseCase,
    private val saveRecentSurahUseCase: SaveRecentSurahUseCase,
    private val deleteRecentSurahUseCase: DeleteRecentSurahUseCase,
    private val dataStorePreference: DataStorePreference? = null
) : ViewModel() {

    private val _state = MutableStateFlow(AyahState())
    val state: StateFlow<AyahState> = _state.asStateFlow()

    // Once the user finishes the surah, block any further saves so exiting
    // the screen doesn't re-insert the surah back into recent reads.
    private var isCompleted = false

    init {
        loadAyah()
    }

    private fun loadAyah() {
        viewModelScope.launch(Dispatchers.IO) {
            getSurahAyahsUseCase(surahNumber = surahNumber).collectLatest { ayahModels ->
                _state.update {
                    it.copy(ayahs = ayahModels)
                }
            }
        }
    }

    fun saveLastReadAyah(surahName: String, ayahNumber: Int) {
        if (isCompleted) return  // Surah already finished — do not re-add to recents
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                saveRecentSurahUseCase(
                    surahNumber = surahNumber,
                    surahName = surahName,
                    ayahNumber = ayahNumber,
                    formattedDate = formattedDate
                )
                dataStorePreference?.saveQuranLastRead(
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber
                )
            } catch (e: Exception) {
                // Handle fallback
            }
        }
    }

    /**
     * Called when the user has scrolled to the very end of the Surah and lingered there.
     * Removes this Surah from the recent reads list — it's been completed.
     */
    fun onSurahCompleted() {
        isCompleted = true  // Block any further saves immediately
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteRecentSurahUseCase(surahNumber = surahNumber)
            } catch (e: Exception) {
                // Ignore — non-critical
            }
        }
    }
}