package com.hazrat.islam24.auth.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.islam24.auth.api.VercelApi
import com.hazrat.islam24.auth.repository.ForgetPasswordRepository
import com.hazrat.islam24.auth.repository.ForgetPasswordRepositoryImpl
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.auth.repository.ProfileRepositoryImpl
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.util.ConnectivityObserver
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
        api: VercelApi,
        connectivityObserver: ConnectivityObserver
    ): ForgetPasswordRepository {
        return ForgetPasswordRepositoryImpl(api, connectivityObserver)
    }

    @Singleton
    @Provides
    fun provideVercelApi():VercelApi{
        return  Retrofit.Builder()
            .baseUrl("https://islam24.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VercelApi::class.java)
    }

}