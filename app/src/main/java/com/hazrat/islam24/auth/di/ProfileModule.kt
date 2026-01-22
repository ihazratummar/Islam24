package com.hazrat.islam24.auth.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.islam24.auth.api.Islam24BackendApi
import com.hazrat.islam24.auth.repository.ForgetPasswordRepository
import com.hazrat.islam24.auth.repository.ForgetPasswordRepositoryImpl
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.auth.repository.ProfileRepositoryImpl
import com.hazrat.islam24.auth.repository.SyncRepository
import com.hazrat.islam24.auth.repository.SyncRepositoryImpl
import com.hazrat.islam24.core.domain.repository.QiblaRepository
import com.hazrat.islam24.core.domain.repository.QuranRepository
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.Constants.ISLAM24_BACKEND_BASE_URL
import com.hazrat.zakat.domain.repository.ZakatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(
        @ApplicationContext context: Context,
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        coroutineScope: CoroutineScope,
        syncRepository: SyncRepository,
        connectivityObserver: ConnectivityObserver
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            context = context,
            auth = auth,
            fireStore = firestore,
            storage = storage,
            coroutineScope = coroutineScope,
            syncRepository = syncRepository,
            connectivityObserver = connectivityObserver
        )
    }

    @Singleton
    @Provides
    fun provideForgetPasswordRepository(
        api: Islam24BackendApi,
        connectivityObserver: ConnectivityObserver
    ): ForgetPasswordRepository {
        return ForgetPasswordRepositoryImpl(api, connectivityObserver)
    }

    @Singleton
    @Provides
    fun provideRenderApi(): Islam24BackendApi {

        return Retrofit.Builder()
            .baseUrl(ISLAM24_BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Islam24BackendApi::class.java)
    }


    @Provides
    @Singleton
    fun provideSyncRepository(
        zakatRepository: ZakatRepository,
        quranRepository: QuranRepository,
        qiblaRepository: QiblaRepository
    ): SyncRepository {
        return SyncRepositoryImpl(
            zakatRepository = zakatRepository,
            quranRepository = quranRepository,
            qiblaRepository = qiblaRepository
        )
    }

}