package com.hazrat.islam24.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "names")
data class NameEntity(
    @PrimaryKey
    val number: Int,
    val enDec: String,
    val meaning:String,
    val found:String,
    val name: String,
    val transliteration: String
)
