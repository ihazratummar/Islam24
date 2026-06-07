package com.hazrat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athkar_data")
data class AthkarDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val number: Int,
    val type: String,
    val bismillah: String,
    val arabicText: String,
    val enTranslation: String,
    val enTransliteration: String,
    val bnTranslation: String,
    val bnTransliteration: String,
    val count: Int
)
