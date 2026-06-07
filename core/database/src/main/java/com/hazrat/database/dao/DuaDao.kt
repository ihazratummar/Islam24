package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.hazrat.database.entity.DuaCategoryEntity
import com.hazrat.database.entity.DuaItemEntity
import com.hazrat.database.entity.quran.AyahEntity
import com.hazrat.database.entity.quran.SurahEntity
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 27/01/26
 */

@Dao
interface DuaDao {

    @Query("SELECT * FROM dua_category ORDER BY id ASC")
    fun getDuaCategory(): Flow<List<DuaCategoryEntity>>

    @Query("SELECT * FROM dua_item WHERE categoryId = :categoryId")
    fun getDua(categoryId: Int) : Flow<List<DuaItemEntity>>

    @Query("""
    SELECT * FROM dua_category
    WHERE title LIKE '%' || :title || '%'
    """)
    fun searchDuaCategory(title: String) : Flow<List<DuaCategoryEntity>>

}