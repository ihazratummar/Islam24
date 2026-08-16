package com.hazrat.domain.repository.quran

import com.hazrat.model.quran.KhatamPlanModel
import kotlinx.coroutines.flow.Flow

interface KhatamRepository {
    fun getActiveKhatamPlan(): Flow<KhatamPlanModel?>
    fun getKhatamPlanHistory(): Flow<List<KhatamPlanModel>>
    suspend fun startKhatamPlan(targetEndDateTimestamp: Long)
    suspend fun updateKhatamProgress(surahNumber: Int, ayahNumber: Int, globalAyahNumber: Int)
    suspend fun updateKhatamTargetDate(planId: String, newTargetDateTimestamp: Long)
    suspend fun resetKhatamPlan(planId: String)
    suspend fun endKhatamPlan(planId: String)

    suspend fun clearLocalQuranKhatam()
}