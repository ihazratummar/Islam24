package com.hazrat.islam24.core.di

import android.content.Context
import androidx.room.Room
import com.hazrat.islam24.core.data.dao.ZakatDao
import com.hazrat.islam24.core.data.database.ZakatDatabase
import com.hazrat.islam24.core.data.repository.ZakatRepositoryImpl
import com.hazrat.islam24.core.domain.repository.ZakatRepository
import com.hazrat.islam24.util.Constants.NISAB_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ZakatModule {


    @Singleton
    @Provides
    fun provideNisabDatabase(@ApplicationContext context: Context) : ZakatDatabase {
        return Room.databaseBuilder(
            context,
            ZakatDatabase::class.java,
            NISAB_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideRepository(dao: ZakatDao): ZakatRepository {
        return ZakatRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: ZakatDatabase): ZakatDao {
        return appDatabase.zakatDao()
    }

}