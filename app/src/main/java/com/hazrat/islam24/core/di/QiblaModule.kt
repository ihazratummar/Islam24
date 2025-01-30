package com.hazrat.islam24.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.islam24.core.data.repository.QiblaRepositoryImpl
import com.hazrat.islam24.core.domain.repository.QiblaRepository
import com.hazrat.islam24.util.datastore.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QiblaModule {

    @Provides
    @Singleton
    fun provideQiblaRepository(
        userDataStore: UserDataStore,
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): QiblaRepository {
        return QiblaRepositoryImpl(
            userDataStore = userDataStore,
            firebaseAuth = firebaseAuth,
            firebaseFirestore = firebaseFirestore
        )
    }


}