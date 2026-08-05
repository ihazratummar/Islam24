package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.profile.UserSupportStatusEntity
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 08/08/26
 */

@Dao
interface UserSupportStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupporterStatus(userSupportStatusEntity: UserSupportStatusEntity)

    @Query("SELECT * FROM user_support_status WHERE id = 1")
    fun getSupportDetails() : Flow<UserSupportStatusEntity?>

    @Query("DELETE FROM user_support_status WHERE id = 1")
    suspend fun deleteUserStatus()

}