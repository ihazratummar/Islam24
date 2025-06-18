package com.hazrat.islam24.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hazrat.islam24.core.domain.model.al_quran_model.FavoritesList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.IOException
import java.io.File
import java.io.FileInputStream
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


    /*
    Save Favorite Ayah to file
     */

    fun saveFavoriteAyaToFile(
        context: Context,
        parentFolderName: String,
        subFolderName: String,
        fileName: String,
        favoritesList: FavoritesList
    ) {
        val parentFolder = File(context.filesDir, parentFolderName)
        if (!parentFolder.exists()) parentFolder.mkdirs()

        val subFolder = File(parentFolder, subFolderName)
        if (!subFolder.exists()) subFolder.mkdirs()

        val file = File(subFolder, fileName)
        val json = Gson().toJson(favoritesList)
        file.writeText(json)
    }

    fun getFavoriteAyaToFile(
        context: Context,
        folderName: String,
        fileName: String
    ): FavoritesList {
        val file = File(context.filesDir, folderName + File.separator + fileName)
        if (!file.exists()) return emptyList()
        val json = file.readText()
        val type = object : TypeToken<FavoritesList>() {}.type
        return Gson().fromJson(json, type)

    }


    /*
    Save File to local file
     */

    suspend fun saveFile(
        context: Context,
        parentFolderName: String,
        subFolderName: String,
        fileName: String,
        body: ResponseBody?
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

    /*
    Save Mp3 Files
     */

    fun saveMp3File(
        context: Context,
        sourceFilePath: String, // Path of existing MP3 file in internal storage
        parentFolderName: String,
        subFolderName: String,
        fileName: String
    ): Boolean {
        val sourceFile = File(sourceFilePath) // Use absolute path

        Log.d("saveMp3File", "Checking source file at: ${sourceFile.absolutePath}")

        if (!sourceFile.exists()) {
            Log.e("saveMp3File", "Source file does not exist: ${sourceFile.absolutePath}")
            return false
        }

        val parentFolder = File(context.filesDir, parentFolderName)
        if (!parentFolder.exists()) parentFolder.mkdirs()

        val subFolder = File(parentFolder, subFolderName)
        if (!subFolder.exists()) subFolder.mkdirs()

        val destinationFile = File(subFolder, fileName)

        return try {
            FileInputStream(sourceFile).use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            Log.d("saveMp3File", "File successfully saved to: ${destinationFile.absolutePath}")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("saveMp3File", "Error saving file: ${e.message}")
            false
        }
    }
}