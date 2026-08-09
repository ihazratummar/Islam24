package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hazrat.database.entity.profile.UserEntity
import com.hazrat.database.entity.profile.UserSupportStatusEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID


/**
 * @author hazratummar
 * Created on 07/08/26
 */

@Dao
interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteUser()

}