package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.zakat.NisabEntity
import com.hazrat.database.entity.zakat.ZakatEntity
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
    fun getNisab(): Flow<NisabEntity?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZakatDetails(zakatEntity: ZakatEntity)

    @Query("DELETE FROM zakatentity WHERE id =:zakatId")
    suspend fun deleteZakatDetails(zakatId: String)

    @Query("SELECT * FROM zakatentity ORDER BY date DESC")
    fun getZakatList(): Flow<List<ZakatEntity>>

    @Query("SELECT * FROM zakatentity WHERE id =:id")
    fun getZakatDetails(id: String): Flow<ZakatEntity>

    @Query("SELECT * FROM zakatentity ORDER BY date DESC")
    fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>>

    @Query("SELECT * FROM zakatentity ORDER BY date ASC")
    fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>>


}