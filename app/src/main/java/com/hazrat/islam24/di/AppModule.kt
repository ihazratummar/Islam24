package com.hazrat.islam24.di

import android.content.Context
import androidx.room.Room
import com.hazrat.islam24.data.namesofallah.NameDao
import com.hazrat.islam24.data.namesofallah.NamesDataBase
import com.hazrat.islam24.domain.repository.namesofallah.NamesRepository
import com.hazrat.islam24.network.namesofallah.NamesApi
import com.hazrat.islam24.util.Constants.BASE_URL_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNamesRepository(api: NamesApi, nameDao: NameDao) = NamesRepository(api, nameDao)

    @Singleton
    @Provides
    fun provideNamesApi(): NamesApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL_NAME)
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
    fun provideNameDao(dataBase: NamesDataBase): NameDao{
        return dataBase.nameDao()
    }

}