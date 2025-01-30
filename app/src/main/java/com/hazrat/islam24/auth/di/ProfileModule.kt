package com.hazrat.islam24.auth.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.islam24.auth.api.RenderApi
import com.hazrat.islam24.auth.repository.ForgetPasswordRepository
import com.hazrat.islam24.auth.repository.ForgetPasswordRepositoryImpl
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.auth.repository.ProfileRepositoryImpl
import com.hazrat.islam24.auth.repository.SyncRepository
import com.hazrat.islam24.auth.repository.SyncRepositoryImpl
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.QiblaRepository
import com.hazrat.islam24.core.domain.repository.QuranRepository
import com.hazrat.islam24.core.domain.repository.ZakatRepository
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.Constants.RENDER_BASE_URL
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
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(
        @ApplicationContext context: Context,
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        networkRepository: NetworkRepository
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            context = context, auth = auth, fireStore = firestore,
            storage = storage, networkRepository = networkRepository
        )
    }

    @Singleton
    @Provides
    fun provideForgetPasswordRepository(
        api: RenderApi,
        connectivityObserver: ConnectivityObserver
    ): ForgetPasswordRepository {
        return ForgetPasswordRepositoryImpl(api, connectivityObserver)
    }

    @Singleton
    @Provides
    fun provideRenderApi():RenderApi{

        return  Retrofit.Builder()
            .baseUrl(RENDER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RenderApi::class.java)
    }


    @Provides
    @Singleton
    fun provideSyncRepository(
        zakatRepository: ZakatRepository,
        quranRepository: QuranRepository,
        qiblaRepository: QiblaRepository
    ): SyncRepository {
        return SyncRepositoryImpl(
            zakatRepository =zakatRepository,
            quranRepository = quranRepository,
            qiblaRepository = qiblaRepository
        )
    }

}