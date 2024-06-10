package com.hazrat.islam24.di

import android.content.Context
import androidx.room.Room
import com.hazrat.islam24.data.dao.NameDao
import com.hazrat.islam24.data.database.NamesDataBase
import com.hazrat.islam24.domain.repository.NamesRepository
import com.hazrat.islam24.network.NamesApi
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.Constants.BASE_URL_NAME
import com.hazrat.islam24.util.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideNamesRepository(api: NamesApi, nameDao: NameDao) = NamesRepository(api, nameDao)

    @Singleton
    @Provides
    fun provideNamesApi(): NamesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_NAME)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NamesApi::class.java)
    }

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