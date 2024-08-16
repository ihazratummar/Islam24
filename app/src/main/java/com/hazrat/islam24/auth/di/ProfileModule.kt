package com.hazrat.islam24.auth.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.auth.repository.ProfileRepositoryImpl
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
        connectivityObserver: ConnectivityObserver
    ): ProfileRepository {
        return ProfileRepositoryImpl(context = context, auth = auth, fireStore = firestore, storage = storage, connectivityObserver = connectivityObserver)
    }

}