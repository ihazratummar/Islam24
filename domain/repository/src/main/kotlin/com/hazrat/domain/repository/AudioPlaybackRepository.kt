package com.hazrat.domain.repository

import kotlinx.coroutines.flow.StateFlow

/**
 * Domain Repository Interface for Quran Audio Service background playback.
 *
 * @author hazratummar
 */
interface AudioPlaybackRepository {
    val playbackState: StateFlow<AudioPlaybackState>
    fun startAudio(surahName: String, surahNumber: Int, ayahNumber: Int, globalAyahNumber: Int, totalAyahInSurah: Int)
    fun pauseAudio()
    fun resumeAudio()
    fun playNext()
    fun playPrevious()
    fun stopAudio()
}

data class AudioPlaybackState(
    val surahName: String = "",
    val surahNumber: Int = 1,
    val ayahNumber: Int = 1,
    val globalAyahNumber: Int = 1,
    val totalAyahInSurah: Int = 286,
    val isPlaying: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f,
    val playingAudioPath: String? = null,
    val isActive: Boolean = false
)
