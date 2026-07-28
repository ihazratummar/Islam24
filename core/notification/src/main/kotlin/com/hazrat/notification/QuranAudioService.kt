package com.hazrat.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import com.hazrat.downloader.AudioDownloadState
import com.hazrat.downloader.AudioDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Industry-Grade Foreground Audio Service running playback engine independently of UI lifecycle.
 * Features: Background playback, auto-advancing to next Ayah, System Media Notification controls,
 * MediaSession lockscreen controls, deep linking to exact Ayah, and Bluetooth headphone integration.
 *
 * @author hazratummar
 */
class QuranAudioService : Service() {

    private var mediaSession: MediaSessionCompat? = null
    private val audioDownloader: AudioDownloader by inject()
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var audioPlayer: QuranAudioPlayer? = null
    private var downloadJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        audioPlayer = QuranAudioPlayer(this)
        createNotificationChannel()
        setupMediaSession()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val surahName = intent?.getStringExtra(EXTRA_SURAH_NAME) ?: _serviceState.value.surahName
        val surahNumber = intent?.getIntExtra(EXTRA_SURAH_NUMBER, 1) ?: _serviceState.value.surahNumber
        val ayahNumber = intent?.getIntExtra(EXTRA_AYAH_NUMBER, 1) ?: _serviceState.value.ayahNumber
        val globalAyahNumber = intent?.getIntExtra(EXTRA_GLOBAL_AYAH_NUMBER, 1) ?: _serviceState.value.globalAyahNumber
        val totalAyahInSurah = intent?.getIntExtra(EXTRA_TOTAL_AYAH, 286) ?: _serviceState.value.totalAyahInSurah
        val speed = intent?.getFloatExtra(EXTRA_PLAYBACK_SPEED, _serviceState.value.playbackSpeed) ?: _serviceState.value.playbackSpeed
        val mode = intent?.getStringExtra(EXTRA_PLAYBACK_MODE) ?: _serviceState.value.playbackMode

        when (action) {
            ACTION_START -> {
                _serviceState.update { it.copy(playbackMode = mode) }
                startAudioForAyah(surahName, surahNumber, ayahNumber, globalAyahNumber, totalAyahInSurah)
            }
            ACTION_TOGGLE_PLAY_PAUSE -> {
                togglePlayPause()
            }
            ACTION_PAUSE -> {
                pauseAudio()
            }
            ACTION_RESUME -> {
                resumeAudio()
            }
            ACTION_NEXT_AYAH -> {
                playNextAyah()
            }
            ACTION_PREV_AYAH -> {
                playPreviousAyah()
            }
            ACTION_SET_SPEED -> {
                setPlaybackSpeed(speed)
            }
            ACTION_SET_MODE -> {
                _serviceState.update { it.copy(playbackMode = mode) }
            }
            ACTION_STOP -> {
                stopPlaybackAndService()
            }
        }
        return START_NOT_STICKY
    }

    private fun setPlaybackSpeed(speed: Float) {
        _serviceState.update { it.copy(playbackSpeed = speed) }
        audioPlayer?.setSpeed(speed)
    }

    private fun startAudioForAyah(
        surahName: String,
        surahNumber: Int,
        ayahNumber: Int,
        globalAyahNumber: Int,
        totalAyahInSurah: Int
    ) {
        downloadJob?.cancel()

        // 1. Instant Cache Check for zero-latency / real-time playback
        val cachedFile = audioDownloader.getCachedFile(globalAyahNumber)

        if (cachedFile != null) {
            _serviceState.update {
                it.copy(
                    surahName = surahName,
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    globalAyahNumber = globalAyahNumber,
                    totalAyahInSurah = totalAyahInSurah,
                    isDownloading = false,
                    isPlaying = true,
                    isActive = true,
                    playingAudioPath = cachedFile.absolutePath
                )
            }
            updateNotificationAndSession(surahName, ayahNumber, isPlaying = true)

            // Trigger background prefetch for next 5 Ayahs to keep buffer filled
            serviceScope.launch {
                audioDownloader.prefetchBatch(globalAyahNumber + 1, count = 5)
            }

            // Play audio instantly from cached file with single reusable player reset
            audioPlayer?.playFile(
                file = cachedFile,
                onCompletion = {
                    playNextAyah()
                },
                onError = {
                    stopPlaybackAndService()
                }
            )
            return
        }

        // 2. File not yet cached: download with progress updates
        _serviceState.update {
            it.copy(
                surahName = surahName,
                surahNumber = surahNumber,
                ayahNumber = ayahNumber,
                globalAyahNumber = globalAyahNumber,
                totalAyahInSurah = totalAyahInSurah,
                isDownloading = true,
                isPlaying = false,
                isActive = true
            )
        }

        updateNotificationAndSession(surahName, ayahNumber, isPlaying = false)

        // Download & Play Current Ayah
        downloadJob = serviceScope.launch {
            audioDownloader.downloadAudio(globalAyahNumber).collect { downloadState ->
                when (downloadState) {
                    is AudioDownloadState.Downloading -> {
                        _serviceState.update {
                            it.copy(isDownloading = true, downloadProgress = downloadState.progress)
                        }
                    }
                    is AudioDownloadState.Success -> {
                        _serviceState.update {
                            it.copy(
                                isDownloading = false,
                                isPlaying = true,
                                playingAudioPath = downloadState.file.absolutePath
                            )
                        }
                        updateNotificationAndSession(surahName, ayahNumber, isPlaying = true)

                        // Prefetch next 5 ayahs in background once current Ayah is ready
                        serviceScope.launch {
                            audioDownloader.prefetchBatch(globalAyahNumber + 1, count = 5)
                        }

                        // Play audio via native MediaPlayer engine
                        audioPlayer?.playFile(
                            file = downloadState.file,
                            onCompletion = {
                                playNextAyah()
                            },
                            onError = {
                                stopPlaybackAndService()
                            }
                        )
                    }
                    is AudioDownloadState.Error -> {
                        _serviceState.update {
                            it.copy(isDownloading = false, isPlaying = false)
                        }
                        serviceScope.launch(Dispatchers.Main) {
                            val msg = if (downloadState.isNetworkError) {
                                "Internet not available. Please check your connection."
                            } else {
                                downloadState.message
                            }
                            android.widget.Toast.makeText(
                                this@QuranAudioService,
                                msg,
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        }
                        stopPlaybackAndService()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun togglePlayPause() {
        if (_serviceState.value.isPlaying) {
            pauseAudio()
        } else {
            resumeAudio()
        }
    }

    private fun pauseAudio() {
        audioPlayer?.pause()
        _serviceState.update { it.copy(isPlaying = false) }
        updateNotificationAndSession(_serviceState.value.surahName, _serviceState.value.ayahNumber, isPlaying = false)
    }

    private fun resumeAudio() {
        audioPlayer?.resume()
        _serviceState.update { it.copy(isPlaying = true) }
        updateNotificationAndSession(_serviceState.value.surahName, _serviceState.value.ayahNumber, isPlaying = true)
    }

    private fun playNextAyah() {
        val currentState = _serviceState.value
        when (currentState.playbackMode) {
            "REPEAT_AYAH" -> {
                startAudioForAyah(
                    surahName = currentState.surahName,
                    surahNumber = currentState.surahNumber,
                    ayahNumber = currentState.ayahNumber,
                    globalAyahNumber = currentState.globalAyahNumber,
                    totalAyahInSurah = currentState.totalAyahInSurah
                )
            }
            "PLAY_JUZ" -> {
                if (currentState.ayahNumber < currentState.totalAyahInSurah) {
                    startAudioForAyah(
                        surahName = currentState.surahName,
                        surahNumber = currentState.surahNumber,
                        ayahNumber = currentState.ayahNumber + 1,
                        globalAyahNumber = currentState.globalAyahNumber + 1,
                        totalAyahInSurah = currentState.totalAyahInSurah
                    )
                } else if (currentState.surahNumber < 114) {
                    val nextSurah = currentState.surahNumber + 1
                    val nextSurahName = com.hazrat.ui.common.SurahNameProvider.getSurahName(nextSurah)
                    val nextSurahTotalAyahs = com.hazrat.ui.common.SurahNameProvider.getSurahTotalAyahs(nextSurah)
                    startAudioForAyah(
                        surahName = nextSurahName,
                        surahNumber = nextSurah,
                        ayahNumber = 1,
                        globalAyahNumber = currentState.globalAyahNumber + 1,
                        totalAyahInSurah = nextSurahTotalAyahs
                    )
                } else {
                    stopPlaybackAndService()
                }
            }
            else -> { // PLAY_SURAH
                if (currentState.ayahNumber < currentState.totalAyahInSurah) {
                    startAudioForAyah(
                        surahName = currentState.surahName,
                        surahNumber = currentState.surahNumber,
                        ayahNumber = currentState.ayahNumber + 1,
                        globalAyahNumber = currentState.globalAyahNumber + 1,
                        totalAyahInSurah = currentState.totalAyahInSurah
                    )
                } else {
                    stopPlaybackAndService()
                }
            }
        }
    }

    private fun playPreviousAyah() {
        val currentState = _serviceState.value
        if (currentState.ayahNumber > 1) {
            startAudioForAyah(
                surahName = currentState.surahName,
                surahNumber = currentState.surahNumber,
                ayahNumber = currentState.ayahNumber - 1,
                globalAyahNumber = currentState.globalAyahNumber - 1,
                totalAyahInSurah = currentState.totalAyahInSurah
            )
        }
    }

    private fun updateNotificationAndSession(surahName: String, ayahNumber: Int, isPlaying: Boolean) {
        updatePlaybackState(isPlaying)
        val notification = buildMediaNotification(surahName, ayahNumber, isPlaying)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun setupMediaSession() {
        mediaSession = MediaSessionCompat(this, "QuranAudioService").apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    togglePlayPause()
                }

                override fun onPause() {
                    togglePlayPause()
                }

                override fun onSkipToNext() {
                    playNextAyah()
                }

                override fun onSkipToPrevious() {
                    playPreviousAyah()
                }

                override fun onStop() {
                    stopPlaybackAndService()
                }
            })
            isActive = true
        }
    }

    private fun updatePlaybackState(isPlaying: Boolean) {
        val state = if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
        mediaSession?.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PAUSE or
                            PlaybackStateCompat.ACTION_PLAY_PAUSE or
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                            PlaybackStateCompat.ACTION_STOP
                )
                .setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1.0f)
                .build()
        )
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Quran Recitation Playback",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows Quran Ayah recitation background player controls"
            setShowBadge(false)
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun buildMediaNotification(
        surahName: String,
        ayahNumber: Int,
        isPlaying: Boolean
    ): android.app.Notification {
        val surahNum = _serviceState.value.surahNumber
        val displaySurahName = if (surahName.isNotBlank() && !surahName.startsWith("Surah")) {
            surahName
        } else {
            com.hazrat.ui.common.SurahNameProvider.getSurahName(surahNum)
        }

        // Explicit intent with extras — reliable with singleTask launch mode
        val contentIntent = Intent().apply {
            setClassName(packageName, "com.hazrat.islam24.main.mainActivity.MainActivity")
            putExtra("extra_nav_surah_number", surahNum)
            putExtra("extra_nav_ayah_number", ayahNumber)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingContentIntent = PendingIntent.getActivity(
            this,
            surahNum * 1000 + ayahNumber, // Unique request code per ayah
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseAction = if (isPlaying) {
            NotificationCompat.Action(
                android.R.drawable.ic_media_pause,
                "Pause",
                getBroadcastPendingIntent(ACTION_TOGGLE_PLAY_PAUSE)
            )
        } else {
            NotificationCompat.Action(
                android.R.drawable.ic_media_play,
                "Play",
                getBroadcastPendingIntent(ACTION_TOGGLE_PLAY_PAUSE)
            )
        }

        val prevAction = NotificationCompat.Action(
            android.R.drawable.ic_media_previous,
            "Previous",
            getBroadcastPendingIntent(ACTION_PREV_AYAH)
        )

        val nextAction = NotificationCompat.Action(
            android.R.drawable.ic_media_next,
            "Next",
            getBroadcastPendingIntent(ACTION_NEXT_AYAH)
        )

        val stopAction = NotificationCompat.Action(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Close",
            getBroadcastPendingIntent(ACTION_STOP)
        )

        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession?.sessionToken)
            .setShowActionsInCompactView(0, 1, 2)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("$displaySurahName - Aya $ayahNumber")
            .setContentText("Mishary Rashid Alafasy • Islam24")
            .setSubText("Quran Recitation")
            .setContentIntent(pendingContentIntent)
            .setOngoing(isPlaying)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(mediaStyle)
            .addAction(prevAction)
            .addAction(playPauseAction)
            .addAction(nextAction)
            .addAction(stopAction)
            .build()
    }

    private fun getBroadcastPendingIntent(action: String): PendingIntent {
        val intent = Intent(action).apply { setPackage(packageName) }
        return PendingIntent.getBroadcast(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun stopPlaybackAndService() {
        downloadJob?.cancel()
        audioPlayer?.stop()
        _serviceState.update { QuranServiceState() }
        mediaSession?.isActive = false
        mediaSession?.release()
        mediaSession = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        stopPlaybackAndService()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "quran_recitation_channel"
        const val NOTIFICATION_ID = 2001

        const val ACTION_START = "com.hazrat.islam24.ACTION_START_AUDIO"
        const val ACTION_TOGGLE_PLAY_PAUSE = "com.hazrat.islam24.ACTION_TOGGLE_PLAY_PAUSE"
        const val ACTION_PAUSE = "com.hazrat.islam24.ACTION_PAUSE_AUDIO"
        const val ACTION_RESUME = "com.hazrat.islam24.ACTION_RESUME_AUDIO"
        const val ACTION_NEXT_AYAH = "com.hazrat.islam24.ACTION_NEXT_AYAH"
        const val ACTION_PREV_AYAH = "com.hazrat.islam24.ACTION_PREV_AYAH"
        const val ACTION_STOP = "com.hazrat.islam24.ACTION_STOP_AUDIO"
        const val ACTION_SET_SPEED = "com.hazrat.islam24.ACTION_SET_SPEED"
        const val ACTION_SET_MODE = "com.hazrat.islam24.ACTION_SET_MODE"

        const val EXTRA_SURAH_NAME = "extra_surah_name"
        const val EXTRA_SURAH_NUMBER = "extra_surah_number"
        const val EXTRA_AYAH_NUMBER = "extra_ayah_number"
        const val EXTRA_GLOBAL_AYAH_NUMBER = "extra_global_ayah_number"
        const val EXTRA_TOTAL_AYAH = "extra_total_ayah"
        const val EXTRA_PLAYBACK_SPEED = "extra_playback_speed"
        const val EXTRA_PLAYBACK_MODE = "extra_playback_mode"

        private val _serviceState = MutableStateFlow(QuranServiceState())
        val serviceState: StateFlow<QuranServiceState> = _serviceState.asStateFlow()

        fun startService(
            context: Context,
            surahName: String,
            surahNumber: Int,
            ayahNumber: Int,
            globalAyahNumber: Int,
            totalAyahInSurah: Int,
            mode: String = "PLAY_SURAH"
        ) {
            val intent = Intent(context, QuranAudioService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_SURAH_NAME, surahName)
                putExtra(EXTRA_SURAH_NUMBER, surahNumber)
                putExtra(EXTRA_AYAH_NUMBER, ayahNumber)
                putExtra(EXTRA_GLOBAL_AYAH_NUMBER, globalAyahNumber)
                putExtra(EXTRA_TOTAL_AYAH, totalAyahInSurah)
                putExtra(EXTRA_PLAYBACK_MODE, mode)
            }
            context.startForegroundService(intent)
        }

        fun sendAction(context: Context, action: String) {
            val intent = Intent(context, QuranAudioService::class.java).apply {
                this.action = action
            }
            context.startService(intent)
        }
    }
}

data class QuranServiceState(
    val surahName: String = "",
    val surahNumber: Int = 1,
    val ayahNumber: Int = 1,
    val globalAyahNumber: Int = 1,
    val totalAyahInSurah: Int = 286,
    val isPlaying: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f,
    val playingAudioPath: String? = null,
    val isActive: Boolean = false,
    val playbackSpeed: Float = 1.0f,
    val playbackMode: String = "PLAY_SURAH"
)
