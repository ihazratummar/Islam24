package com.hazrat.islam24.core.di

import android.content.Context
import androidx.room.Room
import com.hazrat.islam24.core.api.AthkarApiCall
import com.hazrat.islam24.core.data.dao.NameDao
import com.hazrat.islam24.core.data.database.NamesDataBase
import com.hazrat.islam24.core.data.repository.NamesRepositoryImpl
import com.hazrat.islam24.core.data.repository.NetworkRepositoryImpl
import com.hazrat.islam24.core.domain.repository.NamesRepository
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.api.NamesApi
import com.hazrat.islam24.core.data.dao.AthkarDao
import com.hazrat.islam24.core.data.database.AthkarDatabase
import com.hazrat.islam24.core.data.repository.AthkarRepositoryImpl
import com.hazrat.islam24.core.domain.repository.AthkarRepository
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.ContextUtils
import com.hazrat.islam24.util.DataStorePreference
import com.hazrat.islam24.util.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideNamesRepository(api: NamesApi, nameDao: NameDao): NamesRepository =
        NamesRepositoryImpl(api, nameDao)


    /*
    name database
     */
    @Singleton
    @Provides
    fun provideNamesDatabase(@ApplicationContext context: Context): NamesDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            NamesDataBase::class.java,
            "names_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNameDao(dataBase: NamesDataBase): NameDao {
        return dataBase.nameDao()
    }


    /*
    Athkar database
     */
    @Singleton
    @Provides
    fun provideAthkarDatabase(@ApplicationContext context: Context): AthkarDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AthkarDatabase::class.java,
            "athkar_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideAthkarDao(dataBase: AthkarDatabase): AthkarDao {
        return dataBase.athkarDao()
    }

    @Singleton
    @Provides
    fun provideAthkarRepository(api: AthkarApiCall, dao: AthkarDao): AthkarRepository =
        AthkarRepositoryImpl(api, dao)


    @Singleton
    @Provides
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }


    @Singleton
    @Provides
    fun provideContextUtil(@ApplicationContext context: Context): Context {
        return  ContextUtils(context)
    }


    @Singleton
    @Provides
    fun provideNetworkRepository(
        networkConnectivityObserver: ConnectivityObserver,
    ): NetworkRepository {
        return NetworkRepositoryImpl(networkConnectivityObserver)
    }


    @Provides
    @Singleton
    fun provideDataStorePreference(@ApplicationContext context: Context): DataStorePreference {
        return DataStorePreference(context)
    }
}