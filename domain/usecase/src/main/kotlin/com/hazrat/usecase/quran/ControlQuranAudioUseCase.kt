package com.hazrat.usecase.quran

import com.hazrat.domain.repository.AudioPlaybackRepository
import com.hazrat.domain.repository.AudioPlaybackState
import kotlinx.coroutines.flow.StateFlow

/**
 * Dedicated UseCase for controlling Quran Audio Service background playback.
 *
 * @author hazratummar
 */
class ControlQuranAudioUseCase(
    private val audioPlaybackRepository: AudioPlaybackRepository
) {
    val playbackState: StateFlow<AudioPlaybackState>
        get() = audioPlaybackRepository.playbackState

    fun startAudio(surahName: String, surahNumber: Int, ayahNumber: Int, globalAyahNumber: Int, totalAyahInSurah: Int, mode: String = "PLAY_SURAH") {
        audioPlaybackRepository.startAudio(surahName, surahNumber, ayahNumber, globalAyahNumber, totalAyahInSurah, mode)
    }

    fun setSpeed(speed: Float) {
        audioPlaybackRepository.setPlaybackSpeed(speed)
    }

    fun pauseAudio() {
        audioPlaybackRepository.pauseAudio()
    }

    fun resumeAudio() {
        audioPlaybackRepository.resumeAudio()
    }

    fun playNext() {
        audioPlaybackRepository.playNext()
    }

    fun playPrevious() {
        audioPlaybackRepository.playPrevious()
    }

    fun stopAudio() {
        audioPlaybackRepository.stopAudio()
    }
}
