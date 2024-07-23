package com.hazrat.islam24.core.di

import com.hazrat.islam24.core.network.GregorianToHijriApi
import com.hazrat.islam24.core.network.LocationNameApi
import com.hazrat.islam24.core.network.NamesApi
import com.hazrat.islam24.core.network.PrayerTimeApi
import com.hazrat.islam24.util.Constants.BASE_URL
import com.hazrat.islam24.util.Constants.BASE_URL_NAME
import com.hazrat.islam24.util.Constants.GTH_BASE_URL
import com.hazrat.islam24.util.Constants.LOCATION_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()


    //Prayer Time Api
    @Singleton
    @Provides
    fun providePrayerApi(): PrayerTimeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrayerTimeApi::class.java)
    }


    //LocationApi Name
    @Singleton
    @Provides
    fun provideLocationNameApi(): LocationNameApi {
        return Retrofit.Builder()
            .baseUrl(LOCATION_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationNameApi::class.java)
    }


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
    fun provideGregorianToHijriApi(): GregorianToHijriApi {
        return Retrofit.Builder()
            .baseUrl(GTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GregorianToHijriApi::class.java)
    }

}