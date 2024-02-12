package com.hazrat.islam24.data.namesofallah

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hazrat.islam24.data.namesofallah.NameDao
import com.hazrat.islam24.data.namesofallah.NameEntity


@Database(entities = [NameEntity::class], version = 2, exportSchema =false )
abstract class NamesDataBase : RoomDatabase() {

    abstract fun nameDao(): NameDao
}