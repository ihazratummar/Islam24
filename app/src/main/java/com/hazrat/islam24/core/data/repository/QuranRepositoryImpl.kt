package com.hazrat.islam24.core.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hazrat.islam24.core.api.QuranApi
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.LocalQuranModelArItem
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_en.LocalQuranDataEnItem
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_json_bn.LocalQuranDataItemBn
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_transliteration.LocalQuranTransliterationItem
import com.hazrat.islam24.core.domain.repository.QuranRepository
import com.hazrat.islam24.core.presentation.al_quran.QuranState
import com.hazrat.islam24.util.DataStorePreference
import com.hazrat.islam24.util.MyFileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-12-2024
 */

class QuranRepositoryImpl @Inject constructor(
    private val quranApi: QuranApi,
    private val fileUtils: MyFileUtils,
    @ApplicationContext private val context: Context,
    private val dataStorePreference: DataStorePreference
) : QuranRepository {

    private val _quranState = MutableStateFlow(QuranState())
    override val quranState: StateFlow<QuranState> = _quranState.asStateFlow()

    override suspend fun downloadQuranFile() {
        if (!fileUtils.isFilePresent(
                context,
                PARENT_FOLDER_NAME_DOWNLOAD,
                QURAN_FOLDER_NAME,
                QURAN_AR_FILE_NAME
            ) ||
            !fileUtils.isFilePresent(
                context,
                PARENT_FOLDER_NAME_DOWNLOAD,
                QURAN_FOLDER_NAME,
                QURAN_TRANSLITERATION_FILE_NAME
            ) ||
            !fileUtils.isFilePresent(
                context,
                PARENT_FOLDER_NAME_DOWNLOAD,
                QURAN_FOLDER_NAME,
                QURAN_EN_FILE_NAME
            ) ||
            !fileUtils.isFilePresent(
                context,
                PARENT_FOLDER_NAME_DOWNLOAD,
                QURAN_FOLDER_NAME,
                QURAN_BN_FILE_NAME
            )
        ) {
            Log.d(TAG, "One or more JSON files not found. Downloading missing files...")
            downloadQuranFile(PARENT_FOLDER_NAME_DOWNLOAD,QURAN_FOLDER_NAME)
        } else {
            Log.d(TAG, "All JSON files already exist.")
        }
    }


    private suspend fun downloadQuranFile(parentFolderName: String, subFolderName: String) {
        val filesToDownload = listOf(
            QURAN_AR_FILE_NAME to quranApi::downloadQuranArFile,
            QURAN_TRANSLITERATION_FILE_NAME to quranApi::downloadQuranTransliterationFile,
            QURAN_EN_FILE_NAME to quranApi::downloadQuranEnFile,
            QURAN_BN_FILE_NAME to quranApi::downloadQuranBnFile
        )

        filesToDownload.forEach { (fileName, downloadFunction) ->
            if (!fileUtils.isFilePresent(context, parentFolderName, subFolderName, fileName)) {
                try {
                    val response = downloadFunction()
                    if (response.isSuccessful && response.body() != null) {
                        val isSaved = fileUtils.saveFile(
                            context = context,
                            parentFolderName = parentFolderName,
                            subFolderName = subFolderName,
                            fileName = fileName,
                            body = response.body()
                        )
                        if (isSaved) {
                            Log.d(TAG, "$fileName downloaded and saved successfully.")
                        } else {
                            Log.e(TAG, "Failed to save $fileName.")
                        }
                    } else {
                        Log.e(
                            TAG,
                            "Failed to download $fileName. Response code: ${response.code()}"
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error downloading $fileName: ${e.message}")
                }
            } else {
                Log.d(TAG, "$fileName already exists.")
            }
        }
    }


    override fun readQuranArFile(): Flow<List<LocalQuranModelArItem>>? = flow {

        val jsonString = fileUtils.readFile(
            context = context,
            folderName = "${PARENT_FOLDER_NAME_DOWNLOAD}/${QURAN_FOLDER_NAME}",
            fileName = QURAN_AR_FILE_NAME
        )
        if (jsonString != null) {
            val data: List<LocalQuranModelArItem> = Gson().fromJson(
                jsonString,
                object : TypeToken<List<LocalQuranModelArItem>>() {}.type
            )
            _quranState.update { it.copy(arQuranData = data) }
            emit(data)
        } else {
            Log.e(TAG, "JSON file not found.")
            emit(emptyList())
        }

    }.catch { e ->
        Log.e(TAG, "Error in flow: ${e.message}")
        emit(emptyList()) // Emit empty list if an exception occurs
    }.flowOn(Dispatchers.IO)

    override fun readQuranTransliterationFile(): Flow<List<LocalQuranTransliterationItem>>? = flow {
        val jsonString = fileUtils.readFile(
            context = context,
            folderName = "${PARENT_FOLDER_NAME_DOWNLOAD}/${QURAN_FOLDER_NAME}" ,
            fileName = QURAN_TRANSLITERATION_FILE_NAME
        )
        if (jsonString != null) {
            val data: List<LocalQuranTransliterationItem> = Gson().fromJson(
                jsonString,
                object : TypeToken<List<LocalQuranTransliterationItem>>() {}.type
            )
            emit(data)
            _quranState.update { it.copy(quranTransliterationData = data) }
        } else {
            Log.e(TAG, "JSON file not found.")
            emit(emptyList())
        }
    }.catch { e ->
        Log.e(TAG, "Error in flow: ${e.message}")
        emit(emptyList()) // Emit empty list if an exception occurs
    }.flowOn(Dispatchers.IO)

    override fun readQuranEnFile(): Flow<List<LocalQuranDataEnItem>>? = flow {
        val jsonString = fileUtils.readFile(
            context = context,
            folderName = "${PARENT_FOLDER_NAME_DOWNLOAD}/${QURAN_FOLDER_NAME}",
            fileName = QURAN_EN_FILE_NAME
        )
        if (jsonString != null) {
            val data: List<LocalQuranDataEnItem> = Gson().fromJson(
                jsonString,
                object : TypeToken<List<LocalQuranDataEnItem>>() {}.type
            )
            emit(data)
            _quranState.update { it.copy(quranEnData = data) }
        } else {
            Log.e(TAG, "JSON file not found.")
            emit(emptyList())
        }
    }.catch { e ->
        Log.e(TAG, "Error in flow: ${e.message}")
        emit(emptyList()) // Emit empty list if an exception occurs
    }.flowOn(Dispatchers.IO)

    override fun readQuranBnFile(): Flow<List<LocalQuranDataItemBn>> = flow {
        val jsonString = fileUtils.readFile(
            context = context,
            folderName = "${PARENT_FOLDER_NAME_DOWNLOAD}/${QURAN_FOLDER_NAME}",
            fileName = QURAN_BN_FILE_NAME
        )
        if (jsonString != null) {
            val data: List<LocalQuranDataItemBn> = Gson().fromJson(
                jsonString,
                object : TypeToken<List<LocalQuranDataItemBn>>() {}.type
            )
            _quranState.update { it.copy(quranBnData = data) }
            emit(data) // Emit the parsed data
        } else {
            Log.e(TAG, "JSON file not found.")
            emit(emptyList()) // Emit an empty list if the file is not found
        }
    }.catch { e ->
        Log.e(TAG, "Error in flow: ${e.message}")
        emit(emptyList()) // Emit empty list if an exception occurs
    }.flowOn(Dispatchers.IO)


    override suspend fun getAllQuranData() {
        _quranState.value = _quranState.value.copy(isLoading = true)
        combine(
            readQuranArFile() ?: flow { emit(emptyList()) },
            readQuranEnFile() ?: flow { emit(emptyList()) },
            readQuranBnFile(),
            readQuranTransliterationFile() ?: flow { emit(emptyList()) }
        ) { ar, en, bn, transliteration ->
            _quranState.value = _quranState.value.copy(
                isLoading = false,
                arQuranData = ar,
                quranEnData = en,
                quranBnData = bn,
                quranTransliterationData = transliteration
            )
        }.collect{}
    }

    override fun saveLastRead(surahNumber: Int, ayahNumber: Int) {
        dataStorePreference.saveQuranLastRead(surahNumber, ayahNumber)
        _quranState.update {
            it.copy(
                lastReadSurah = surahNumber,
                lastReadAyah = ayahNumber
            )
        }
    }


    override fun refreshLastRead() {
        _quranState.update {
            it.copy(
                lastReadSurah = dataStorePreference.getQuranLastRead().first,
                lastReadAyah = dataStorePreference.getQuranLastRead().second,
                lastReadSurahName = getSurahNameByNumber(),
            )
        }
    }

    private fun getSurahNameByNumber(): String?{
        if (_quranState.value.arQuranData?.isEmpty() != false) {
            Log.e("QuranViewModel", "Quran data is not loaded.")
            return null
        }
        return _quranState.value.arQuranData?.find { it.number == _quranState.value.lastReadSurah }?.englishName
    }

    companion object {
        private const val TAG = "QuranRepositoryImpl"
        private const val PARENT_FOLDER_NAME_DOWNLOAD = "Download"
        private const val QURAN_FOLDER_NAME = "AlQuran"
        private const val QURAN_AR_FILE_NAME = "quran_ar.json"
        private const val QURAN_TRANSLITERATION_FILE_NAME = "quran_transliteration.json"
        private const val QURAN_EN_FILE_NAME = "quran_en.json"
        private const val QURAN_BN_FILE_NAME = "quran_bn.json"

    }
}