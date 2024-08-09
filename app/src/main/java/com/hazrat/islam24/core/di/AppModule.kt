package com.hazrat.islam24.core.di

import android.content.Context
import android.content.ContextWrapper
import androidx.room.Room
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.core.data.dao.NameDao
import com.hazrat.islam24.core.data.database.NamesDataBase
import com.hazrat.islam24.core.data.manager.NamesRepositoryImpl
import com.hazrat.islam24.core.domain.repository.NamesRepository
import com.hazrat.islam24.core.network.NamesApi
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.Constants.BASE_URL_NAME
import com.hazrat.islam24.util.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideNamesRepository(api: NamesApi, nameDao: NameDao): NamesRepository =
        NamesRepositoryImpl(api, nameDao)



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


    @Singleton
    @Provides
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

}