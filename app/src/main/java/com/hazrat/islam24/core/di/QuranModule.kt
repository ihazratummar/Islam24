package com.hazrat.islam24.core.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.islam24.core.remote.api.QuranApi
import com.hazrat.islam24.core.data.repository.QuranRepositoryImpl
import com.hazrat.islam24.core.domain.repository.QuranRepository
import com.hazrat.islam24.util.datastore.DataStorePreference
import com.hazrat.islam24.util.MyFileUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuranModule {

    @Provides
    @Singleton
    fun provideQuranRepository(
        quranApi: QuranApi,
        fileUtils: MyFileUtils,
        @ApplicationContext context: Context,
        dataStorePreference: DataStorePreference,
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): QuranRepository {
        return QuranRepositoryImpl(
            context = context,
            quranApi = quranApi,
            fileUtils = fileUtils,
            dataStorePreference = dataStorePreference,
            firebaseAuth = firebaseAuth,
            firebaseFirestore = firebaseFirestore
        )
    }

}