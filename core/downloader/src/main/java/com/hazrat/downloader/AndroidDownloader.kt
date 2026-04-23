package com.hazrat.downloader

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.hazrat.downloader.MyFileUtils.saveFile
import com.hazrat.utils.Constants.DOWNLOADED_AZAN_FOLDER
import com.hazrat.utils.Constants.PARENT_FOLDER_NAME_DOWNLOAD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 09-02-2025
 */

class AndroidDownloader(
    private val context: Context
) : Downloader {
    private val client = OkHttpClient()

    override suspend fun downloadFile(url: String, mimeType: String, title: String): Long {
        return withContext(Dispatchers.IO) {
            val fileName = url.toUri().lastPathSegment ?: "Downloaded_File"
            val request = Request.Builder().url(url).build()

            try {
                client.newCall(request).execute().use { response ->
                    Log.d("FileCheck", "Response Code: ${response.code}")
                    Log.d("FileCheck", "Response Message: ${response.message}")
                    Log.d("FileCheck", "Response Headers: ${response.headers}")

                    if (!response.isSuccessful) {
                        throw IOException("Download failed: ${response.code} ${response.message}")
                    }

                    val contentLength = response.body.contentLength()
                    Log.d("FileCheck", "Expected Content-Length: $contentLength")

                    response.body.use { body ->
                        val isSaving = saveFile(
                            context = context,
                            parentFolderName = PARENT_FOLDER_NAME_DOWNLOAD,
                            subFolderName = DOWNLOADED_AZAN_FOLDER,
                            fileName = fileName,
                            body = body,
                        )
                        if (isSaving) contentLength else 0L // Return actual size
                    }
                }
            } catch (e: IOException) {
                Log.e("AndroidDownloader", "Download or save failed: ${e.message}", e)
                0L // Indicate failure
            }
        }
    }
}