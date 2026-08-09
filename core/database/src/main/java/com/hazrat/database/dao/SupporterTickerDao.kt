package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.profile.SupporterTickerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for managing cached community supporter tickers (max 10 records).
 * @author Hazrat Ummar Shaikh
 */
@Dao
interface SupporterTickerDao {

    @Query("SELECT * FROM supporter_ticker ORDER BY id DESC LIMIT 10")
    fun getRecentTickers(): Flow<List<SupporterTickerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicker(ticker: SupporterTickerEntity)

    @Query("DELETE FROM supporter_ticker WHERE id NOT IN (SELECT id FROM supporter_ticker ORDER BY id DESC LIMIT 10)")
    suspend fun trimOldTickers()
}
