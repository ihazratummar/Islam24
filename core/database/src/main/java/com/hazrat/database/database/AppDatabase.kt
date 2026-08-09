package com.hazrat.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hazrat.database.converter.DatabaseConverters
import com.hazrat.database.dao.SupporterTickerDao
import com.hazrat.database.dao.UserDao
import com.hazrat.database.dao.UserSupportStatusDao
import com.hazrat.database.entity.profile.SupporterTickerEntity
import com.hazrat.database.entity.profile.UserEntity
import com.hazrat.database.entity.profile.UserSupportStatusEntity


/**
 * @author hazratummar
 * Created on 07/08/26
 */

@Database(
    entities = [UserEntity::class, UserSupportStatusEntity::class, SupporterTickerEntity::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userSupportStatusDao(): UserSupportStatusDao
    abstract fun supporterTickerDao(): SupporterTickerDao
}