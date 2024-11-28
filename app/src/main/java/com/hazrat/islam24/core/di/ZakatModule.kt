package com.hazrat.islam24.core.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.islam24.BuildConfig
import com.hazrat.islam24.core.data.dao.ZakatDao
import com.hazrat.islam24.core.data.database.ZakatDatabase
import com.hazrat.islam24.core.data.repository.ZakatRepositoryImpl
import com.hazrat.islam24.core.domain.repository.ZakatRepository
import com.hazrat.islam24.util.Constants.NISAB_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ZakatModule {



    @Singleton
    @Provides
    fun provideNisabDatabase(@ApplicationContext context: Context): ZakatDatabase {
        val passPhrase = SQLiteDatabase.getBytes(BuildConfig.MY_PASS_PHRASE.toCharArray())
        val factory = SupportFactory(passPhrase)



        return Room.databaseBuilder(
            context,
            ZakatDatabase::class.java,
            NISAB_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

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

    @Provides
    @Singleton
    fun provideDao(appDatabase: ZakatDatabase): ZakatDao {
        return appDatabase.zakatDao()
    }

}