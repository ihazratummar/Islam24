package com.hazrat.islam24.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hazrat.islam24.util.Constants.APP_DATA_STORE
import com.hazrat.islam24.util.Constants.USER_DATA_SORE
import com.hazrat.islam24.util.downloader.AndroidDownloader
import com.hazrat.islam24.util.downloader.Downloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named(APP_DATA_STORE)
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(APP_DATA_STORE)
        }
    }

    @Singleton
    @Provides
    @Named(USER_DATA_SORE)
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(USER_DATA_SORE)
        }
    }

    @Singleton
    @Provides
    fun provideDownloader(
        @ApplicationContext context: Context
    ): Downloader {
        return AndroidDownloader(context)
    }


}