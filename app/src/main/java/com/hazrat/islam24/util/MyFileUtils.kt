package com.hazrat.islam24.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

object MyFileUtils {


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
                        inputStream.copyTo(outputStream)
                    }
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
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