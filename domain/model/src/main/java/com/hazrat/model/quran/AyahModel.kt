package com.hazrat.model.quran

data class AyahModel(
    val id: Int,
    val surahNumber: Int,
    val globalAyahNumber: Int,
    val arabicText: String,
    val ayahNumber: Int,
    val englishTranslation: String,
    val transliteration: String,
    val isBookmarked: Boolean = false,
    val tajweedText: String = "",
    val bnMuhiuddin: String = "",
    val bnTaisirul: String = "",
    val bnMujibur: String = "",
    val bnTransliteration: String = ""
) {
    fun getTranslation(source: String): String {
        return when (source) {
            "MUHIUDDIN" -> bnMuhiuddin.ifBlank { englishTranslation }
            "TAISIRUL" -> bnTaisirul.ifBlank { bnMuhiuddin.ifBlank { englishTranslation } }
            "MUJIBUR" -> bnMujibur.ifBlank { bnMuhiuddin.ifBlank { englishTranslation } }
            "ENGLISH" -> englishTranslation.ifBlank { bnMuhiuddin }
            else -> bnMuhiuddin.ifBlank { englishTranslation }
        }
    }

    fun getActiveTransliteration(locale: java.util.Locale = java.util.Locale.getDefault()): String {
        val isBengali = locale.language.equals("bn", ignoreCase = true)
        return if (isBengali) {
            bnTransliteration.ifBlank { transliteration }
        } else {
            transliteration.ifBlank { bnTransliteration }
        }
    }
}
