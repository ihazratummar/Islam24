package com.hazrat.di

import android.content.Context
import androidx.room.Room
import com.hazrat.database.database.ZakatDatabase
import com.hazrat.utils.BuildConfig
import com.hazrat.utils.Constants.NISAB_DATABASE_NAME
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
object RoomModule {


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

}