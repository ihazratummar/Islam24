package com.hazrat.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.database.dao.ZakatDao
import com.hazrat.zakat.data.repository.ZakatRepositoryImpl
import com.hazrat.zakat.domain.repository.ZakatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        dao: ZakatDao,
        auth: FirebaseAuth,
        fireStore: FirebaseFirestore,
        @ApplicationContext context: Context
    ): ZakatRepository {
        return ZakatRepositoryImpl(dao = dao, auth = auth, fireStore = fireStore, context = context)
    }

}