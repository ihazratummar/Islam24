package com.hazrat.utils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

object MyFileUtils {

    private const val DEFAULT_BUFFER_SIZE = 8192

    fun isFilePresent(
        context: Context,
        parentFolder: String,
        subFolderName: String,
        fileName: String
    ): Boolean {
        val parentFolder = File(context.filesDir, parentFolder)
        if (!parentFolder.exists()) parentFolder.mkdirs()

        val subFolder = File(parentFolder, subFolderName)
        if (!subFolder.exists()) subFolder.mkdirs()

        val file = File(subFolder, fileName)
        return file.exists()
    }


    suspend fun saveFile(
        context: Context,
        parentFolderName: String,
        subFolderName: String,
        fileName: String,
        body: ResponseBody? = null
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val folder = File(context.filesDir, parentFolderName)
                if (!folder.exists()) folder.mkdirs()

                val subFolder = File(folder, subFolderName)
                if (!subFolder.exists()) subFolder.mkdirs()

                val file = File(subFolder, fileName)

                body?.byteStream()?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var bytesRead: Int
                        var totalBytesRead = 0L
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            totalBytesRead += bytesRead
                        }
                        outputStream.flush() // Ensure all data is written
                        Log.d("FileCheck", "Total bytes read: $totalBytesRead")
                        Log.d("FileCheck", "File size after saving: ${file.length()}")

                        if (totalBytesRead != file.length()) {
                            Log.e("FileCheck", "File size mismatch after saving")
                        }
                    }
                } ?: return@withContext false // Handle null body

                return@withContext true
            } catch (e: Exception) {
                Log.e("FileCheck", "Error saving file: ${e.message}", e)
                return@withContext false
            }
        }
    }


    /*
    Read File from local file
     */

    fun readFile(context: Context, folderName: String, fileName: String): String? {
        val file = File(context.filesDir, folderName + File.separator + fileName)
        return if (file.exists()) {
            file.bufferedReader().use { it.readText() }
        } else {
            null
        }
    }

}