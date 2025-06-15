package com.hazrat.di

import android.util.Printer
import com.hazrat.datastore.AppDataStore
import com.hazrat.usecase.DarkModeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {


    @Singleton
    @Provides
    fun provideDarkModeUseCase(
        appDataStore: AppDataStore
    ): DarkModeUseCase {
        return DarkModeUseCase(appDataStore = appDataStore)
    }

}