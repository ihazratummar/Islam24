package com.hazrat.islam24.core.di

import com.hazrat.islam24.core.api.GregorianToHijriApi
import com.hazrat.islam24.core.api.LocationNameApi
import com.hazrat.islam24.core.api.NamesApi
import com.hazrat.islam24.core.api.PrayerTimeApi
import com.hazrat.islam24.util.Constants.BASE_URL
import com.hazrat.islam24.util.Constants.BASE_URL_NAME
import com.hazrat.islam24.util.Constants.GTH_BASE_URL
import com.hazrat.islam24.util.Constants.LOCATION_IQ_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
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

    private val retryInterceptor = Interceptor { chain ->
        var response = chain.proceed(chain.request())
        var tryCount = 0
        val maxLimit = 3 // Number of retries

        while (!response.isSuccessful && tryCount < maxLimit) {
            tryCount++
            response.close() // Close the previous response before retrying
            response = chain.proceed(chain.request())
        }
        response
    }


    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(retryInterceptor)
        .connectTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()

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

    @Singleton
    @Provides
    fun provideLocationNameApi(): LocationNameApi {
        return Retrofit.Builder()
            .baseUrl(LOCATION_IQ_BASE_URL)
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