package com.hazrat.allahNames.model.namesofallah


data class NameOfAllahData(
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