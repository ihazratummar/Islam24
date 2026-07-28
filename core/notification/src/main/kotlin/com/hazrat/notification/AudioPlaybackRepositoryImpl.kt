package com.hazrat.notification

import android.content.Context
import com.hazrat.domain.repository.AudioPlaybackRepository
import com.hazrat.domain.repository.AudioPlaybackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Implementation of AudioPlaybackRepository connecting domain UseCase to QuranAudioService.
 *
 * @author hazratummar
 */
class AudioPlaybackRepositoryImpl(
    private val context: Context
) : AudioPlaybackRepository {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _playbackState = MutableStateFlow(AudioPlaybackState())
    override val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    init {
        scope.launch {
            QuranAudioService.serviceState.collectLatest { serviceState ->
                _playbackState.value = AudioPlaybackState(
                    surahName = serviceState.surahName,
                    surahNumber = serviceState.surahNumber,
                    ayahNumber = serviceState.ayahNumber,
                    globalAyahNumber = serviceState.globalAyahNumber,
                    totalAyahInSurah = serviceState.totalAyahInSurah,
                    isPlaying = serviceState.isPlaying,
                    isDownloading = serviceState.isDownloading,
                    downloadProgress = serviceState.downloadProgress,
                    playingAudioPath = serviceState.playingAudioPath,
                    isActive = serviceState.isActive,
                    playbackSpeed = serviceState.playbackSpeed,
                    playbackMode = serviceState.playbackMode
                )
            }
        }
    }

    override fun startAudio(
        surahName: String,
        surahNumber: Int,
        ayahNumber: Int,
        globalAyahNumber: Int,
        totalAyahInSurah: Int,
        mode: String
    ) {
        QuranAudioService.startService(
            context = context,
            surahName = surahName,
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            globalAyahNumber = globalAyahNumber,
            totalAyahInSurah = totalAyahInSurah,
            mode = mode
        )
    }

    override fun setPlaybackSpeed(speed: Float) {
        val intent = android.content.Intent(context, QuranAudioService::class.java).apply {
            action = QuranAudioService.ACTION_SET_SPEED
            putExtra(QuranAudioService.EXTRA_PLAYBACK_SPEED, speed)
        }
        context.startService(intent)
    }

    override fun pauseAudio() {
        QuranAudioService.sendAction(context, QuranAudioService.ACTION_PAUSE)
    }

    override fun resumeAudio() {
        QuranAudioService.sendAction(context, QuranAudioService.ACTION_RESUME)
    }

    override fun playNext() {
        QuranAudioService.sendAction(context, QuranAudioService.ACTION_NEXT_AYAH)
    }

    override fun playPrevious() {
        QuranAudioService.sendAction(context, QuranAudioService.ACTION_PREV_AYAH)
    }

    override fun stopAudio() {
        QuranAudioService.sendAction(context, QuranAudioService.ACTION_STOP)
    }
}
