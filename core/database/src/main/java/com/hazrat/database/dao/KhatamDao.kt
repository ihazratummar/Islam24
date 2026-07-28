package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hazrat.database.entity.quran.KhatamPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KhatamDao {

    @Query("SELECT * FROM khatam_plan WHERE status = 'IN_PROGRESS' ORDER BY startDateTimestamp DESC LIMIT 1")
    fun getActiveKhatamPlan(): Flow<KhatamPlanEntity?>

    @Query("SELECT * FROM khatam_plan ORDER BY startDateTimestamp DESC")
    fun getAllKhatamPlans(): Flow<List<KhatamPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateKhatamPlan(plan: KhatamPlanEntity)

    @Query("UPDATE khatam_plan SET status = 'ENDED', updatedTimestamp = :timestamp WHERE id = :id")
    suspend fun endKhatamPlan(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM khatam_plan WHERE id = :id")
    suspend fun deleteKhatamPlan(id: String)
}
