package com.hazrat.remote.di

import com.hazrat.remote.api.AthkarApiCall
import com.hazrat.remote.api.GoogleCloudConsoleApi
import com.hazrat.remote.api.Islam24BackendApi
import com.hazrat.remote.api.LocationNameApi
import com.hazrat.remote.api.NamesApi
import com.hazrat.remote.api.PrayerTimeApi
import com.hazrat.remote.api.QuranApi
import com.hazrat.utils.Constants.ATHKAR_BASE_URL_NAME
import com.hazrat.utils.Constants.BASE_URL_NAME
import com.hazrat.utils.Constants.GOOGLE_CLOUD_BASE_URL
import com.hazrat.utils.Constants.GTH_BASE_URL
import com.hazrat.utils.Constants.ISLAM24_BACKEND_BASE_URL
import com.hazrat.utils.Constants.LOCATION_IQ_BASE_URL
import com.hazrat.utils.Constants.PRAYER_BASE_URL
import com.hazrat.utils.Constants.QURAN_AR_BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


/**
 * @author hazratummar
 * Created on 22/01/26
 */

const val PRAYER_RETROFIT = "PRAYER_RETROFIT"
const val GOOGLE_RETROFIT = "GOOGLE_RETROFIT"
const val LOCATION_RETROFIT = "LOCATION_RETROFIT"
const val ALLAH_NAMES_RETROFIT = "ALLAH_NAMES_RETROFIT"
const val GTH_RETROFIT = "GTH_RETROFIT"
const val ATHKAR_RETROFIT = "ATHKAR_RETROFIT"
const val QURAN_RETROFIT = "QURAN_RETROFIT"

const val ISLAM24_RETROFIT = "ISLAM24_RETROFIT"


fun getRemoteModule(): Module = module {

    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }

    single {
        Interceptor { chain ->
            var response = chain.proceed(chain.request())
            var tryCount = 0
            val maxLimit = 3

            while (!response.isSuccessful && tryCount < maxLimit) {
                tryCount++
                response.close()
                response = chain.proceed(chain.request())
            }
            response
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<Interceptor>())
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    single { Json { ignoreUnknownKeys = true } }

    single(named(PRAYER_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(PRAYER_BASE_URL)
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single(named(GOOGLE_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(GOOGLE_CLOUD_BASE_URL)
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single(named(LOCATION_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(LOCATION_IQ_BASE_URL)
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single(named(ALLAH_NAMES_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BASE_URL_NAME)
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single(named(GTH_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(GTH_BASE_URL)
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single(named(ATHKAR_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(ATHKAR_BASE_URL_NAME)
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single(named(QURAN_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(QURAN_AR_BASE_URL)
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }


    single(named(ISLAM24_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(ISLAM24_BACKEND_BASE_URL)
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }


    // API:

    single<AthkarApiCall> {get<Retrofit>(named(ATHKAR_RETROFIT)).create(AthkarApiCall::class.java) }
    single <GoogleCloudConsoleApi>{ get<Retrofit>(named(GOOGLE_RETROFIT)).create(GoogleCloudConsoleApi::class.java) }
    single <LocationNameApi>{ get<Retrofit>(named(LOCATION_RETROFIT)).create(LocationNameApi::class.java) }
    single <NamesApi>{ get<Retrofit>(named(ALLAH_NAMES_RETROFIT)).create(NamesApi::class.java) }
    single <PrayerTimeApi>{ get<Retrofit>(named(PRAYER_RETROFIT)).create(PrayerTimeApi::class.java) }
    single <QuranApi>{ get<Retrofit>(named(QURAN_RETROFIT)).create(QuranApi::class.java) }
    single <Islam24BackendApi>{ get<Retrofit>(named(ISLAM24_RETROFIT)).create(Islam24BackendApi::class.java) }



}