package com.hazrat.islam24.core.di

import android.app.Application
import androidx.room.Room
import com.hazrat.islam24.data.database.TasbihDatabase
import com.hazrat.islam24.data.manager.TasbihRepositoryImpl
import com.hazrat.islam24.domain.repository.TasbihRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TasbihModule {

    @Provides
    fun provideTasbihDatabase(app:Application): TasbihDatabase {
        return Room.databaseBuilder(
            app,
            TasbihDatabase::class.java,
            "tasbih_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTasbihRepository(database: TasbihDatabase): TasbihRepository {
        return TasbihRepositoryImpl(database.tasbihCounterDao())
    }

}