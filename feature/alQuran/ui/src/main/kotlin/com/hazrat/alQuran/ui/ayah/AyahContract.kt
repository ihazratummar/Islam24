package com.hazrat.alQuran.ui.ayah

import androidx.compose.runtime.Stable
import com.hazrat.model.al_quran_model.AyahModel
import com.hazrat.model.al_quran_model.SurahModel

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
    val playingSurahNumber: Int? = null,
    val playingAyahNumber: Int? = null,
    val playingAudioPath: String? = null,
    val isPlaying: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f,
    val playbackSpeed: Float = 1.0f,
    val audioErrorMessage: String? = null
)

sealed interface AyahUiEvent {
    data class OnAyahClick(val ayah: AyahModel) : AyahUiEvent
    data object OnDismissMenu : AyahUiEvent
    data class OnPlayAyah(val ayahNumber: Int) : AyahUiEvent
    data object OnPauseAudio : AyahUiEvent
    data object OnResumeAudio : AyahUiEvent
    data object OnStopAudio : AyahUiEvent
    data object OnPlayNextAyah : AyahUiEvent
    data object OnPlayPreviousAyah : AyahUiEvent
    data class OnSpeedChange(val speed: Float) : AyahUiEvent
}