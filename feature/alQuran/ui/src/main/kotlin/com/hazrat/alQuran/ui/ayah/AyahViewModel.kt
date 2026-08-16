package com.hazrat.alQuran.ui.ayah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.DataStorePreference
import com.hazrat.model.quran.AyahModel
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
    private val initialTargetAyahNumber: Int = 1,
    private val isFromBookmark: Boolean = false,
    private val isFromKhatam: Boolean = false,
    private val getSurahAyahsUseCase: GetSurahAyahsUseCase,
    private val saveRecentSurahUseCase: SaveRecentSurahUseCase,
    private val deleteRecentSurahUseCase: DeleteRecentSurahUseCase,
    private val dataStorePreference: DataStorePreference? = null,
    private val appDataStore: com.hazrat.datastore.AppDataStore? = null,
    private val controlQuranAudioUseCase: ControlQuranAudioUseCase? = null,
    private val toggleAyahBookmarkUseCase: com.hazrat.usecase.quran.ToggleAyahBookmarkUseCase? = null,
    private val updateKhatamProgressUseCase: com.hazrat.usecase.khatam.UpdateKhatamProgressUseCase? = null
) : ViewModel() {

    private val _state = MutableStateFlow(AyahState())
    val state: StateFlow<AyahState> = _state.asStateFlow()

    private var currentSurahNumber: Int = surahNumber
    private var ayahJob: kotlinx.coroutines.Job? = null
    private var isCompleted = false

    init {
        loadAyah(surahNumber)
        observeBackgroundAudioService()
        observeQuranPreferences()
    }

    private fun observeQuranPreferences() {
        val prefs = appDataStore ?: return
        viewModelScope.launch(Dispatchers.IO) {
            prefs.quranFont.collectLatest { font ->
                _state.update { it.copy(selectedFont = font) }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            prefs.quranFontSize.collectLatest { size ->
                _state.update { it.copy(fontSize = size) }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            prefs.quranShowTranslation.collectLatest { show ->
                _state.update { it.copy(showTranslation = show) }
            }
        }
    }

    private fun loadAyah(targetSurahNumber: Int = currentSurahNumber) {
        currentSurahNumber = targetSurahNumber
        isCompleted = false
        ayahJob?.cancel()
        ayahJob = viewModelScope.launch(Dispatchers.IO) {
            getSurahAyahsUseCase(surahNumber = targetSurahNumber).collectLatest { ayahModels ->
                _state.update {
                    it.copy(ayahs = ayahModels)
                }
                val targetAyahNum = if (targetSurahNumber == surahNumber) initialTargetAyahNumber else 1
                saveLastReadAyah(ayahNumber = targetAyahNum)
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
            is AyahUiEvent.OnPlayAyah -> playAyahWithMode(event.ayahNumber, "PLAY_SURAH")
            is AyahUiEvent.OnPlaySurahFrom -> playAyahWithMode(event.ayahNumber, "PLAY_SURAH")
            is AyahUiEvent.OnPlayJuzFrom -> playAyahWithMode(event.ayahNumber, "PLAY_JUZ")
            is AyahUiEvent.OnRepeatAyah -> playAyahWithMode(event.ayahNumber, "REPEAT_AYAH")
            is AyahUiEvent.OnToggleBookmark -> toggleBookmark(event.ayah)
            is AyahUiEvent.OnPauseAudio -> pauseAudio()
            is AyahUiEvent.OnResumeAudio -> resumeAudio()
            is AyahUiEvent.OnStopAudio -> stopAudioPlayback()
            is AyahUiEvent.OnPlayNextAyah -> playNextAyah()
            is AyahUiEvent.OnPlayPreviousAyah -> playPreviousAyah()
            is AyahUiEvent.OnSpeedChange -> setPlaybackSpeed(event.speed)
            is AyahUiEvent.OnSurahPageChanged -> loadAyah(event.surahNumber)
            is AyahUiEvent.OnToggleSettingsMenu -> {
                _state.update { it.copy(isSettingsMenuOpen = !it.isSettingsMenuOpen) }
            }
            is AyahUiEvent.OnFontSelected -> setQuranFont(event.fontName)
            is AyahUiEvent.OnFontSizeChanged -> setQuranFontSize(event.size)
            is AyahUiEvent.OnToggleTranslation -> setQuranShowTranslation(event.show)
        }
    }

    private fun setQuranFont(fontName: String) {
        // 1. Instant real-time UI update (0ms lag)
        _state.update { it.copy(selectedFont = fontName) }
        // 2. Asynchronous DataStore save
        appDataStore?.let { prefs ->
            viewModelScope.launch(Dispatchers.IO) {
                prefs.saveQuranFont(fontName)
            }
        }
    }

    private fun setQuranFontSize(size: Int) {
        // 1. Instant real-time UI update (0ms lag)
        _state.update { it.copy(fontSize = size) }
        // 2. Asynchronous DataStore save
        appDataStore?.let { prefs ->
            viewModelScope.launch(Dispatchers.IO) {
                prefs.saveQuranFontSize(size)
            }
        }
    }

    private fun setQuranShowTranslation(show: Boolean) {
        // 1. Instant real-time UI update (0ms lag)
        _state.update { it.copy(showTranslation = show) }
        // 2. Asynchronous DataStore save
        appDataStore?.let { prefs ->
            viewModelScope.launch(Dispatchers.IO) {
                prefs.saveQuranShowTranslation(show)
            }
        }
    }

    private fun onAyahClick(ayah: AyahModel) {
        _state.update { it.copy(selectedAyahForMenu = ayah) }
    }

    private fun dismissAyahMenu() {
        _state.update { it.copy(selectedAyahForMenu = null) }
    }

    private fun playAyahWithMode(ayahNumber: Int, mode: String) {
        val activeSurahNum = currentSurahNumber
        val ayahList = _state.value.ayahs
        val targetAyah = ayahList.find { it.ayahNumber == ayahNumber } ?: return
        val surahName = com.hazrat.ui.common.SurahNameProvider.getSurahName(activeSurahNum)

        _state.update {
            it.copy(
                playingSurahNumber = activeSurahNum,
                playingAyahNumber = ayahNumber,
                selectedAyahForMenu = null
            )
        }

        controlQuranAudioUseCase?.startAudio(
            surahName = surahName,
            surahNumber = activeSurahNum,
            ayahNumber = ayahNumber,
            globalAyahNumber = targetAyah.globalAyahNumber,
            totalAyahInSurah = ayahList.size,
            mode = mode
        )
    }

    private fun toggleBookmark(ayah: AyahModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val newBookmarkState = !ayah.isBookmarked
            toggleAyahBookmarkUseCase?.invoke(
                surahNumber = ayah.surahNumber,
                ayahNumber = ayah.ayahNumber,
                isBookmarked = newBookmarkState
            )
            dismissAyahMenu()
        }
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
        controlQuranAudioUseCase?.setSpeed(speed)
    }

    fun saveLastReadAyah(surahName: String = "", ayahNumber: Int) {
        if (isCompleted || isFromBookmark) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isFromKhatam) {
                    val targetAyah = _state.value.ayahs.find { it.ayahNumber == ayahNumber }
                    if (targetAyah != null && targetAyah.globalAyahNumber > 0) {
                        updateKhatamProgressUseCase?.invoke(
                            surahNumber = currentSurahNumber,
                            ayahNumber = ayahNumber,
                            globalAyahNumber = targetAyah.globalAyahNumber
                        )
                    }
                } else {
                    val resolvedSurahName = if (surahName.isNotBlank()) surahName else com.hazrat.ui.common.SurahNameProvider.getSurahName(currentSurahNumber)
                    val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                    saveRecentSurahUseCase(
                        surahNumber = currentSurahNumber,
                        surahName = resolvedSurahName,
                        ayahNumber = ayahNumber,
                        formattedDate = formattedDate
                    )
                    dataStorePreference?.saveQuranLastRead(
                        surahNumber = currentSurahNumber,
                        ayahNumber = ayahNumber
                    )
                }
            } catch (_: Exception) {}
        }
    }

    fun onSurahCompleted() {
        if (isCompleted) return
        isCompleted = true
        if (!isFromKhatam) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    deleteRecentSurahUseCase(currentSurahNumber)
                    dataStorePreference?.saveQuranLastRead(
                        surahNumber = 0,
                        ayahNumber = 0
                    )
                } catch (_: Exception) {}
            }
        }
    }
}