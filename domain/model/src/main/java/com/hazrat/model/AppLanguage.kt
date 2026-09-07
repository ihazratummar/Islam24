package com.hazrat.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String
) {
    ENGLISH("en", "English", "English"),
    BENGALI("bn", "Bengali", "বাংলা");

    companion object {
        fun fromCode(code: String?): AppLanguage {
            if (code.isNullOrEmpty()) return ENGLISH
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
