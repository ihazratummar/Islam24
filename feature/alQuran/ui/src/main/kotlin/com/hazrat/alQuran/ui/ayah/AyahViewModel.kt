package com.hazrat.alQuran.ui.ayah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.DataStorePreference
import com.hazrat.model.al_quran_model.AyahModel
import com.hazrat.usecase.quran.ControlQuranAudioUseCase
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
 * AyahViewModel managing Ayah text loading, menu selection, and observing background audio service state.
 *
 * @author hazratummar
 */
class AyahViewModel(
    private val surahNumber: Int,
    private val getSurahAyahsUseCase: GetSurahAyahsUseCase,
    private val saveRecentSurahUseCase: SaveRecentSurahUseCase,
    private val deleteRecentSurahUseCase: DeleteRecentSurahUseCase,
    private val dataStorePreference: DataStorePreference? = null,
    private val controlQuranAudioUseCase: ControlQuranAudioUseCase? = null
) : ViewModel() {

    private val _state = MutableStateFlow(AyahState())
    val state: StateFlow<AyahState> = _state.asStateFlow()

    private var isCompleted = false

    init {
        loadAyah()
        observeBackgroundAudioService()
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

    private fun observeBackgroundAudioService() {
        val useCase = controlQuranAudioUseCase ?: return
        viewModelScope.launch(Dispatchers.IO) {
            useCase.playbackState.collectLatest { serviceState ->
                if (serviceState.isActive) {
                    _state.update {
                        it.copy(
                            playingSurahNumber = serviceState.surahNumber,
                            playingAyahNumber = serviceState.ayahNumber,
                            isPlaying = serviceState.isPlaying,
                            isDownloading = serviceState.isDownloading,
                            downloadProgress = serviceState.downloadProgress,
                            playingAudioPath = serviceState.playingAudioPath
                        )
                    }
                } else if (_state.value.playingAyahNumber != null) {
                    _state.update {
                        it.copy(
                            playingSurahNumber = null,
                            playingAyahNumber = null,
                            isPlaying = false,
                            isDownloading = false,
                            downloadProgress = 0f,
                            playingAudioPath = null
                        )
                    }
                }
            }
        }
    }

    /**
     * Single MVI Event Handler entry point.
     */
    fun onEvent(event: AyahUiEvent) {
        when (event) {
            is AyahUiEvent.OnAyahClick -> onAyahClick(event.ayah)
            is AyahUiEvent.OnDismissMenu -> dismissAyahMenu()
            is AyahUiEvent.OnPlayAyah -> playAyah(event.ayahNumber)
            is AyahUiEvent.OnPauseAudio -> pauseAudio()
            is AyahUiEvent.OnResumeAudio -> resumeAudio()
            is AyahUiEvent.OnStopAudio -> stopAudioPlayback()
            is AyahUiEvent.OnPlayNextAyah -> playNextAyah()
            is AyahUiEvent.OnPlayPreviousAyah -> playPreviousAyah()
            is AyahUiEvent.OnSpeedChange -> setPlaybackSpeed(event.speed)
        }
    }

    private fun onAyahClick(ayah: AyahModel) {
        _state.update { it.copy(selectedAyahForMenu = ayah) }
    }

    private fun dismissAyahMenu() {
        _state.update { it.copy(selectedAyahForMenu = null) }
    }

    private fun playAyah(ayahNumber: Int) {
        val ayahList = _state.value.ayahs
        val targetAyah = ayahList.find { it.ayahNumber == ayahNumber } ?: return
        val surahName = com.hazrat.ui.common.SurahNameProvider.getSurahName(surahNumber)

        _state.update {
            it.copy(
                playingSurahNumber = surahNumber,
                playingAyahNumber = ayahNumber,
                selectedAyahForMenu = null
            )
        }

        controlQuranAudioUseCase?.startAudio(
            surahName = surahName,
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            globalAyahNumber = targetAyah.globalAyahNumber,
            totalAyahInSurah = ayahList.size
        )
    }

    private fun pauseAudio() {
        controlQuranAudioUseCase?.pauseAudio()
    }

    private fun resumeAudio() {
        controlQuranAudioUseCase?.resumeAudio()
    }

    private fun stopAudioPlayback() {
        _state.update {
            it.copy(
                playingSurahNumber = null,
                playingAyahNumber = null,
                playingAudioPath = null,
                isPlaying = false,
                isDownloading = false,
                downloadProgress = 0f
            )
        }
        controlQuranAudioUseCase?.stopAudio()
    }

    private fun playNextAyah() {
        controlQuranAudioUseCase?.playNext()
    }

    private fun playPreviousAyah() {
        controlQuranAudioUseCase?.playPrevious()
    }

    private fun setPlaybackSpeed(speed: Float) {
        _state.update { it.copy(playbackSpeed = speed) }
    }

    fun saveLastReadAyah(surahName: String, ayahNumber: Int) {
        if (isCompleted) return
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
            } catch (_: Exception) {}
        }
    }

    fun onSurahCompleted() {
        isCompleted = true
    }
}