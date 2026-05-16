package com.hazrat.alQuran.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import com.hazrat.alQuran.data.mapper.toModel
import com.hazrat.database.dao.QuranDao
import com.hazrat.database.entity.quran.FavoriteAyahEntity
import com.hazrat.datastore.DataStorePreference
import com.hazrat.domain.repository.QuranRepository
import com.hazrat.model.al_quran_model.FavoriteAyah
import com.hazrat.model.al_quran_model.QuranMetaDataJuz
import com.hazrat.model.al_quran_model.X1
import com.hazrat.model.al_quran_model.local_quran_ar.LocalQuranModelArItem
import com.hazrat.model.al_quran_model.local_quran_en.LocalQuranDataEnItem
import com.hazrat.model.al_quran_model.local_quran_json_bn.LocalQuranDataItemBn
import com.hazrat.model.al_quran_model.local_quran_transliteration.LocalQuranTransliterationItem
import com.hazrat.remote.api.QuranApi
import com.hazrat.utils.Constants.PARENT_FOLDER_NAME_DOWNLOAD
import com.hazrat.utils.Constants.USER_COLLECTION
import com.hazrat.utils.MyFileUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.InputStreamReader

/**
 * @author Hazrat Ummar Shaikh
 * Created on 14-12-2024
 */

class QuranRepositoryImpl(
    private val quranApi: QuranApi,
    private val context: Context,
    private val dataStorePreference: DataStorePreference,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val quranDao: QuranDao
) : QuranRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun downloadQuranFile() {
        if (!MyFileUtils.isFilePresent(
                context,
                PARENT_FOLDER_NAME_DOWNLOAD,
                QURAN_FOLDER_NAME,
                QURAN_AR_FILE_NAME
            ) ||
            !MyFileUtils.isFilePresent(
                context,
                PARENT_FOLDER_NAME_DOWNLOAD,
                QURAN_FOLDER_NAME,
                QURAN_TRANSLITERATION_FILE_NAME
            ) ||
            !MyFileUtils.isFilePresent(
                context,
                PARENT_FOLDER_NAME_DOWNLOAD,
                QURAN_FOLDER_NAME,
                QURAN_EN_FILE_NAME
            ) ||
            !MyFileUtils.isFilePresent(
                context,
                PARENT_FOLDER_NAME_DOWNLOAD,
                QURAN_FOLDER_NAME,
                QURAN_BN_FILE_NAME
            )
        ) {
            Timber.tag(TAG).d("One or more JSON files not found. Downloading missing files...")
            downloadQuranFile(PARENT_FOLDER_NAME_DOWNLOAD, QURAN_FOLDER_NAME)
        } else {
            Timber.tag(TAG).d("All JSON files already exist.")
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
            if (!MyFileUtils.isFilePresent(context, parentFolderName, subFolderName, fileName)) {
                try {
                    val response = downloadFunction()
                    if (response.isSuccessful && response.body() != null) {
                        val isSaved = MyFileUtils.saveFile(
                            context = context,
                            parentFolderName = parentFolderName,
                            subFolderName = subFolderName,
                            fileName = fileName,
                            body = response.body(),
                        )
                        if (isSaved) {
                            Timber.tag(TAG).d("$fileName downloaded and saved successfully.")
                        } else {
                            Timber.tag(TAG).e("Failed to save $fileName.")
                        }
                    } else {
                        Timber.tag(TAG)
                            .e("Failed to download $fileName. Response code: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Timber.tag(TAG).e("Error downloading $fileName: ${e.message}")
                }
            } else {
                Timber.tag(TAG).d("$fileName already exists.")
            }
        }
    }


    override fun readQuranArFile(): List<LocalQuranModelArItem> {

        val jsonString = MyFileUtils.readFile(
            context = context,
            folderName = "${PARENT_FOLDER_NAME_DOWNLOAD}/${QURAN_FOLDER_NAME}",
            fileName = QURAN_AR_FILE_NAME
        )
        if (jsonString != null) {
            val data: List<LocalQuranModelArItem> = json.decodeFromString(jsonString)
            return data
        } else {
            Timber.tag(TAG).e("JSON file not found.")
            return emptyList()
        }
    }

    override fun readQuranTransliterationFile():List<LocalQuranTransliterationItem>  {
        val jsonString = MyFileUtils.readFile(
            context = context,
            folderName = "${PARENT_FOLDER_NAME_DOWNLOAD}/${QURAN_FOLDER_NAME}",
            fileName = QURAN_TRANSLITERATION_FILE_NAME
        )
        if (jsonString != null) {
            val data: List<LocalQuranTransliterationItem> = json.decodeFromString(jsonString)
            return data
        } else {
            Timber.tag(TAG).e("JSON file not found.")
            return  emptyList()
        }
    }

    override fun readQuranEnFile(): List<LocalQuranDataEnItem> {
        val jsonString = MyFileUtils.readFile(
            context = context,
            folderName = "${PARENT_FOLDER_NAME_DOWNLOAD}/${QURAN_FOLDER_NAME}",
            fileName = QURAN_EN_FILE_NAME
        )
        if (jsonString != null) {
            val data: List<LocalQuranDataEnItem> = json.decodeFromString(jsonString)
            return data
        } else {
            Timber.tag(TAG).e("JSON file not found.")
            return emptyList()
        }
    }

    override fun readQuranBnFile(): List<LocalQuranDataItemBn> {
        val jsonString = MyFileUtils.readFile(
            context = context,
            folderName = "${PARENT_FOLDER_NAME_DOWNLOAD}/${QURAN_FOLDER_NAME}",
            fileName = QURAN_BN_FILE_NAME
        )
        if (jsonString != null) {
            val data: List<LocalQuranDataItemBn> = json.decodeFromString(jsonString)
            return data
        } else {
            Timber.tag(TAG).e("JSON file not found.")
            return emptyList()// Emit an empty list if the file is not found
        }
    }


    override suspend fun getAllQuranData() {
        readQuranArFile()
        readQuranEnFile()
        readQuranBnFile()
        readQuranTransliterationFile()
        parseJuzJson(context)?.let { juz ->
            Timber.tag(TAG).d("getAllQuranData: $juz")
        }


    }

    override fun readFavorite(): Flow<List<FavoriteAyah>> {
        return quranDao.getAllFavoriteAyah()
            .map { entities ->
                if (entities.isEmpty()) {
                    Timber.tag(TAG).e("Favorite list is empty")
                    emptyList()
                } else {
                    entities.toModel()
                }
            }.catch { e ->
                Timber.tag(TAG).e("Error in flow: ${e.message}")
                emit(emptyList())
            }
    }


    override fun saveLastRead(surahNumber: Int, ayahNumber: Int) {
        dataStorePreference.saveQuranLastRead(surahNumber, ayahNumber)
    }



    override suspend fun toggleFavorite(surahNumber: Int, ayahNumber: Int){

        val existing = quranDao.getFavorite(surahNumber, ayahNumber)

        if (existing != null) {
            // Already favorite → remove
            quranDao.deleteFavoriteAyah(surahNumber, ayahNumber)
        } else {
            // Not favorite → add
            quranDao.insertFavoriteAyah(
                FavoriteAyahEntity(
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber
                )
            )
        }
    }


//
//    private fun getSurahNameByNumber(): String? {
//        if (_quranState.value.arQuranData?.isEmpty() != false) {
//            Log.e("QuranViewModel", "Quran data is not loaded.")
//            return null
//        }
//
//        val systemLanguage = checkSystemLanguage(context)
//        val quranData = if (systemLanguage == "bn") {
//            _quranState.value.quranBnData?.find { quranBn ->
//                quranBn.id == _quranState.value.lastReadSurah
//            }?.transliteration
//        } else {
//            _quranState.value.arQuranData?.find { quranAr ->
//                quranAr.number == _quranState.value.lastReadSurah
//            }?.englishName
//        }
//
//        return quranData
//    }

    override suspend fun syncQuranDataIfLoggedIn() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        try {

            val localLastReadSurah = dataStorePreference.getQuranLastRead().first ?: 1
            val localLastReadAyah = dataStorePreference.getQuranLastRead().second ?: 1

            val documentSnapshot =
                firebaseFirestore.collection(USER_COLLECTION).document(userId).get().await()

            if (documentSnapshot.exists()) {
                val firebaseLastReadSurah = documentSnapshot.getLong("lastReadSurah")?.toInt() ?: 1
                val firebaseLastReadAyah = documentSnapshot.getLong("lastReadAyah")?.toInt() ?: 1

                if (firebaseLastReadSurah != localLastReadSurah || firebaseLastReadAyah != localLastReadAyah) {
                    dataStorePreference.saveQuranLastRead(localLastReadSurah, localLastReadAyah)

                    firebaseFirestore.collection(USER_COLLECTION).document(userId)
                        .update(
                            mapOf(
                                "lastReadSurah" to localLastReadSurah,
                                "lastReadAyah" to localLastReadAyah
                            )
                        ).await()
                    Timber.tag("QuranRepositoryImpl")
                        .d("syncQuranDataIfLoggedIn: Quran Last Read Updated")

                } else {
                    Timber.tag("QuranRepositoryImpl")
                        .d("syncQuranDataIfLoggedIn: Quran Last Read is already updated")
                }
            } else {
                Timber.tag(TAG).w("syncQuranDataIfLoggedIn: User document does not exist")
            }

        } catch (e: Exception) {
            Timber.tag("QuranRepositoryImpl").e("syncQuranDataIfLoggedIn: ${e.message}")
        }
    }

    override suspend fun syncQuranDataOnLogin() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        try {
            val documentSnapshot =
                firebaseFirestore.collection(USER_COLLECTION).document(userId).get().await()
            if (documentSnapshot.exists()) {
                val firestorelastReadSurah = documentSnapshot.getLong("lastReadSurah")?.toInt() ?: 1
                val firestoreLastReadAyah = documentSnapshot.getLong("lastReadAyah")?.toInt() ?: 1
                dataStorePreference.saveQuranLastRead(firestorelastReadSurah, firestoreLastReadAyah)
                Timber.tag(TAG).d("syncQuranDataOnLogin: Quran Last Read Updated")
            } else {
                Timber.tag(TAG).w("syncQuranDataOnLogin: User document does not exist")
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e("syncQuranDataOnLogin: ${e.message}")
        }
    }

    fun parseJuzJson(context: Context): QuranMetaDataJuz? {
        return try {
            // Open the "juz.json" file from the assets folder
            val inputStream = context.assets.open("quran_data/juz.json")
            val reader = InputStreamReader(inputStream)

            // Use json.decodeFromString to parse the JSON into a Map where key is the Juz number (String), and value is the X1 object
            val juzMap: Map<String, X1> = json.decodeFromString(reader.readText())

            // Convert the Map values (X1) into a List
            QuranMetaDataJuz(juzData = juzMap.values.toList())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



    companion object {
        private const val TAG = "QuranRepositoryImpl"
        private const val PARENT_FOLDER_NAME_APPDATA = "AppData"
        private const val QURAN_FOLDER_NAME = "AlQuran"
        private const val QURAN_AR_FILE_NAME = "quran_ar.json"
        private const val QURAN_TRANSLITERATION_FILE_NAME = "quran_transliteration.json"
        private const val QURAN_EN_FILE_NAME = "quran_en.json"
        private const val QURAN_BN_FILE_NAME = "quran_bn.json"
        private const val QURAN_AYAH_FAVORITE_FILE = "quran_favorite.json"

    }
}