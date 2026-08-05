package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hazrat.database.converter.DatabaseConverters
import com.hazrat.database.dao.UserDao
import com.hazrat.database.dao.UserSupportStatusDao
import com.hazrat.database.entity.profile.UserEntity
import com.hazrat.database.entity.profile.UserSupportStatusEntity


/**
 * @author hazratummar
 * Created on 07/08/26
 */

@Database(entities = [UserEntity::class, UserSupportStatusEntity::class], version = 5, exportSchema = false)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun userSupportStatusDao() : UserSupportStatusDao
}