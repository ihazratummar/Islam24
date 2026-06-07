package com.hazrat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "allah_names")
data class AllahNameEntity(
    @PrimaryKey
    val number: Int,
    val enDesc: String,
    val enMeaning:String,
    val found:String,
    val name: String,
    val transliteration: String,
    val bnTransliteration: String,
    val bnMeaning: String,
    val bnDec: String?
)