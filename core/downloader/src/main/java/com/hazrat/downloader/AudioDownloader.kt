package com.hazrat.downloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.hazrat.database.dao.quran.QuranDao
import com.hazrat.database.entity.quran.AudioCacheEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds

sealed class AudioDownloadState {
    object Idle : AudioDownloadState()
    data class Downloading(
        val globalAyahNumber: Int,
        val progress: Float,
        val bytesDownloaded: Long,
        val totalBytes: Long
    ) : AudioDownloadState()
    data class Success(val globalAyahNumber: Int, val file: File) : AudioDownloadState()
    data class Error(val globalAyahNumber: Int, val message: String, val isNetworkError: Boolean) : AudioDownloadState()
}

/**
 * Industry-Grade Audio Downloader with System Notification Progress & Local Room DB Caching.
 *
 * @author hazratummar
 */
class AudioDownloader(
    private val context: Context,
    private val quranDao: QuranDao
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .dispatcher(okhttp3.Dispatcher().apply {
            maxRequests = 20
            maxRequestsPerHost = 10
        })
        .build()

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "quran_audio_downloads"

    private val audioDir: File by lazy {
        File(context.cacheDir, "quran_audio").apply { if (!exists()) mkdirs() }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Quran Recitation Downloads",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification(globalAyahNumber: Int, progressPercent: Int) {
        try {
            createNotificationChannel()
            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("Downloading Ayah Recitation")
                .setContentText("Downloading Ayah $globalAyahNumber ($progressPercent%)")
                .setProgress(100, progressPercent, false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build()
            notificationManager.notify(1001, notification)
        } catch (_: Exception) {}
    }

    private fun cancelNotification() {
        try {
            notificationManager.cancel(1001)
        } catch (_: Exception) {}
    }

    /**
     * Retrieves cached file if present, otherwise downloads from CDN with up to 3 retries.
     */
    fun downloadAudio(
        globalAyahNumber: Int,
        edition: String = "ar.alafasy"
    ): Flow<AudioDownloadState> = flow {
        val fileName = "${edition}_${globalAyahNumber}.mp3"
        val localFile = File(audioDir, fileName)

        // 1. Check DB Cache record
        val dbRecord = quranDao.getAudioCache(globalAyahNumber, edition)
        if (dbRecord != null && localFile.exists() && localFile.length() > 0) {
            emit(AudioDownloadState.Success(globalAyahNumber, localFile))
            return@flow
        }

        // 2. Check File Disk directly
        if (localFile.exists() && localFile.length() > 0) {
            quranDao.insertAudioCache(
                AudioCacheEntity(
                    globalAyahNumber = globalAyahNumber,
                    edition = edition,
                    localPath = localFile.absolutePath,
                    fileSize = localFile.length()
                )
            )
            emit(AudioDownloadState.Success(globalAyahNumber, localFile))
            return@flow
        }

        // 3. Download via HTTP Streaming with progress emission & 3 automatic retries
        var attempts = 0
        var success = false
        var lastErrorMsg = "Network error"

        emit(AudioDownloadState.Downloading(globalAyahNumber, 0f, 0, 100))
        updateNotification(globalAyahNumber, 0)

        while (attempts < 3 && !success) {
            attempts++
            try {
                val url = "https://cdn.islamic.network/quran/audio/128/$edition/$globalAyahNumber.mp3"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val body = response.body
                    val totalBytes = body.contentLength().coerceAtLeast(1)
                    var bytesDownloaded = 0L
                    val buffer = ByteArray(8192)
                    body.byteStream().use { input ->
                        FileOutputStream(localFile).use { output ->
                            var read: Int
                            var lastEmittedPercent = -1
                            while (input.read(buffer).also { read = it } != -1) {
                                output.write(buffer, 0, read)
                                bytesDownloaded += read
                                val progress = (bytesDownloaded.toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f)
                                val percent = (progress * 100).toInt()
                                if (percent != lastEmittedPercent) {
                                    lastEmittedPercent = percent
                                    updateNotification(globalAyahNumber, percent)
                                }
                                emit(AudioDownloadState.Downloading(globalAyahNumber, progress, bytesDownloaded, totalBytes))
                            }
                            output.flush()
                        }
                    }
                    cancelNotification()
                    quranDao.insertAudioCache(
                        AudioCacheEntity(
                            globalAyahNumber = globalAyahNumber,
                            edition = edition,
                            localPath = localFile.absolutePath,
                            fileSize = localFile.length()
                        )
                    )
                    success = true
                    emit(AudioDownloadState.Success(globalAyahNumber, localFile))
                } else {
                    lastErrorMsg = "HTTP ${response.code}"
                }
            } catch (e: Exception) {
                lastErrorMsg = e.localizedMessage ?: "Network connection error"
                if (localFile.exists()) localFile.delete()
                if (attempts < 3) {
                    kotlinx.coroutines.delay(1000.milliseconds)
                }
            }
        }

        if (!success) {
            cancelNotification()
            emit(AudioDownloadState.Error(globalAyahNumber, lastErrorMsg, isNetworkError = true))
        }
    }.flowOn(Dispatchers.IO)

    fun getCachedFile(globalAyahNumber: Int, edition: String = "ar.alafasy"): File? {
        val fileName = "${edition}_${globalAyahNumber}.mp3"
        val localFile = File(audioDir, fileName)
        if (localFile.exists() && localFile.length() > 0) {
            return localFile
        }
        return null
    }

    /**
     * Parallel Batch pre-fetching for instant real-time playback buffer (pre-fetch next N ayahs)
     */
    suspend fun prefetchBatch(
        startGlobalAyah: Int,
        count: Int,
        edition: String = "ar.alafasy"
    ) = withContext(Dispatchers.IO) {
        val batchEnd = (startGlobalAyah + count - 1).coerceAtMost(6236)
        for (globalAyah in startGlobalAyah..batchEnd) {
            val fileName = "${edition}_${globalAyah}.mp3"
            val localFile = File(audioDir, fileName)
            if (localFile.exists() && localFile.length() > 0) continue

            try {
                val url = "https://cdn.islamic.network/quran/audio/128/$edition/$globalAyah.mp3"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val responseBody = response.body
                if (response.isSuccessful) {
                    localFile.outputStream().use { out ->
                        responseBody.byteStream().copyTo(out)
                    }
                    quranDao.insertAudioCache(
                        AudioCacheEntity(
                            globalAyahNumber = globalAyah,
                            edition = edition,
                            localPath = localFile.absolutePath,
                            fileSize = localFile.length()
                        )
                    )
                }
            } catch (_: Exception) {
                // Background prefetch error - ignore and continue
            }
        }
    }
}
