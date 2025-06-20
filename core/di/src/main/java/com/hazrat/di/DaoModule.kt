package com.hazrat.di

import com.hazrat.database.dao.ZakatDao
import com.hazrat.database.database.ZakatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(appDatabase: ZakatDatabase): ZakatDao {
        return appDatabase.zakatDao()
    }

}