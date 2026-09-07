package com.hazrat.alQuran.ui.ayah

import androidx.compose.runtime.Stable
import com.hazrat.model.quran.AyahModel
import com.hazrat.model.quran.SurahModel

/**
 * Ayah Reading Screen Contract & Sealed UI Event.
 *
 * @author hazratummar
 */
@Stable
data class AyahState(
    val ayahs: List<AyahModel> = emptyList(),
    val surahDetails: SurahModel? = null,
    val selectedAyahForMenu: AyahModel? = null,
    val selectedAyahForShare: AyahModel? = null,
    val playingSurahNumber: Int? = null,
    val playingAyahNumber: Int? = null,
    val playingAudioPath: String? = null,
    val isPlaying: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f,
    val playbackSpeed: Float = 1.0f,
    val playbackMode: AudioPlaybackMode = AudioPlaybackMode.PLAY_SURAH,
    val audioErrorMessage: String? = null,
    val selectedFont: String = "SCHEHERAZADE",
    val fontSize: Int = 30,
    val showTranslation: Boolean = true,
    val selectedTranslationSource: String = "MUHIUDDIN",
    val isSettingsMenuOpen: Boolean = false
)

enum class AudioPlaybackMode {
    PLAY_SURAH,
    PLAY_JUZ,
    REPEAT_AYAH
}

sealed interface AyahUiEvent {
    data class OnAyahClick(val ayah: AyahModel) : AyahUiEvent
    data object OnDismissMenu : AyahUiEvent
    data class OnSelectAyahForShare(val ayah: AyahModel) : AyahUiEvent
    data object OnDismissShareDialog : AyahUiEvent
    data class OnPlayAyah(val ayahNumber: Int) : AyahUiEvent
    data class OnPlaySurahFrom(val ayahNumber: Int) : AyahUiEvent
    data class OnPlayJuzFrom(val ayahNumber: Int) : AyahUiEvent
    data class OnRepeatAyah(val ayahNumber: Int) : AyahUiEvent
    data class OnToggleBookmark(val ayah: AyahModel) : AyahUiEvent
    data object OnPauseAudio : AyahUiEvent
    data object OnResumeAudio : AyahUiEvent
    data object OnStopAudio : AyahUiEvent
    data object OnPlayNextAyah : AyahUiEvent
    data object OnPlayPreviousAyah : AyahUiEvent
    data class OnSpeedChange(val speed: Float) : AyahUiEvent
    data class OnSurahPageChanged(val surahNumber: Int) : AyahUiEvent
    data object OnToggleSettingsMenu : AyahUiEvent
    data class OnFontSelected(val fontName: String) : AyahUiEvent
    data class OnFontSizeChanged(val size: Int) : AyahUiEvent
    data class OnToggleTranslation(val show: Boolean) : AyahUiEvent
    data class OnTranslationSourceSelected(val source: String) : AyahUiEvent
}