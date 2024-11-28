package com.hazrat.islam24.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.islam24.core.domain.model.zakat.NisabEntity
import com.hazrat.islam24.core.domain.model.zakat.ZakatEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

@Dao
interface ZakatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNisab(nisabEntity: NisabEntity)

    @Delete
    suspend fun deleteNisab(nisabEntity: NisabEntity)

    @Query("SELECT * FROM nisabentity WHERE id = 1")
    fun getNisab(): Flow<NisabEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZakatDetails(zakatEntity: ZakatEntity)

    @Query("DELETE FROM zakatentity WHERE id =:zakatId")
    suspend fun deleteZakatDetails(zakatId: String)

    @Query("SELECT * FROM zakatentity")
    fun getZakatDetails(): Flow<List<ZakatEntity>>

    @Query("SELECT * FROM zakatentity ORDER BY date DESC")
    fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>>

    @Query("SELECT * FROM zakatentity ORDER BY date ASC")
    fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>>


}