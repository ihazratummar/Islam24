package com.hazrat.tasbih.domain.util

import com.hazrat.tasbih.domain.model.Tasbih
import java.util.Locale

/**
 * Authentic Bengali and English localization helper for Tasbih dhikrs.
 * @author hazratummar
 */
object TasbihLocalization {

    data class LocalizedDhikr(
        val bnTransliteration: String,
        val bnMeaning: String
    )

    private val DHIKR_BN_MAP = mapOf(
        "SubhanAllah" to LocalizedDhikr(
            bnTransliteration = "সুবহানাল্লাহ",
            bnMeaning = "আল্লাহ অতি পবিত্র ও মহিমান্বিত"
        ),
        "Alhamdulillah" to LocalizedDhikr(
            bnTransliteration = "আলহামদুলিল্লাহ",
            bnMeaning = "সকল প্রশংসা একমাত্র আল্লাহর জন্য"
        ),
        "Allahu Akbar" to LocalizedDhikr(
            bnTransliteration = "আল্লাহু আকবার",
            bnMeaning = "আল্লাহ সর্বশ্রেষ্ঠ"
        ),
        "Astaghfirullah wa Atubu Ilayh" to LocalizedDhikr(
            bnTransliteration = "আস্তাগফিরুল্লাহ ওয়া আতূবু ইলাইহি",
            bnMeaning = "আমি আল্লাহর নিকট ক্ষমা প্রার্থনা করছি এবং তাঁরই নিকট তাওবা করছি"
        ),
        "La ilaha illallah" to LocalizedDhikr(
            bnTransliteration = "লা ইলাহা ইল্লাল্লাহ",
            bnMeaning = "আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই"
        ),
        "SubhanAllah wa Bihamdihi" to LocalizedDhikr(
            bnTransliteration = "সুবহানাল্লাহি ওয়া বিহামদিহী",
            bnMeaning = "আমি আল্লাহর প্রশংসাসহ তাঁর পবিত্রতা ঘোষণা করছি"
        ),
        "SubhanAllahi wa Bihamdihi, SubhanAllahil Azeem" to LocalizedDhikr(
            bnTransliteration = "সুবহানাল্লাহি ওয়া বিহামদিহী, সুবহানাল্লাহিল ‘আযীম",
            bnMeaning = "দয়াময়ের নিকট প্রিয় এবং মিযানের পাল্লায় ভারী দুটি বাক্য"
        ),
        "La Hawla Wala Quwwata Illa Billah" to LocalizedDhikr(
            bnTransliteration = "লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ",
            bnMeaning = "আল্লাহর সাহায্য ছাড়া কোনো উপায় ও কোনো শক্তি নেই (জান্নাতের গুপ্তধন)"
        ),
        "Allahumma Salli 'Ala Muhammad" to LocalizedDhikr(
            bnTransliteration = "আল্লাহুম্মা সাল্লি ‘আলা মুহাম্মাদ",
            bnMeaning = "হে আল্লাহ! আপনি মুহাম্মদ ও তাঁর পরিবারের উপর রহমত বর্ষণ করুন"
        ),
        "La Ilaha Illallahu Wahdahu La Sharika Lah" to LocalizedDhikr(
            bnTransliteration = "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু",
            bnMeaning = "একমাত্র আল্লাহ ছাড়া কোনো সত্য ইলাহ নেই, তাঁর কোনো শরীক নেই, রাজত্ব তাঁরই এবং প্রশংসাও তাঁরই"
        ),
        "La Ilaha Illa Anta Subhanaka Inni Kuntu Minaz-Zalimeen" to LocalizedDhikr(
            bnTransliteration = "লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নি কুনতু মিনায যালিমীন",
            bnMeaning = "ইউনুস (আ.)-এর দো‘আ: বিপদ, দুশ্চিন্তা ও কষ্ট থেকে মুক্তির দো‘আ"
        ),
        "Hasbunallahu wa Ni'mal Wakeel" to LocalizedDhikr(
            bnTransliteration = "হাসবুনাল্লাহু ওয়া নি‘মাল ওয়াকীল",
            bnMeaning = "আমাদের জন্য আল্লাহই যথেষ্ট এবং তিনি কতই না উত্তম কর্মবিধায়ক"
        ),
        "SubhanAllahi 'Adada Khalqih" to LocalizedDhikr(
            bnTransliteration = "সুবহানাল্লাহি ওয়া বিহামদিহী ‘আদাদা খালক্বিহী",
            bnMeaning = "আল্লাহর প্রশংসাসহ পবিত্রতা ঘোষণা—তাঁর সৃষ্টির সংখ্যার সমান ও আরশের ওজনের সমান"
        )
    )

    fun getDisplayTransliteration(tasbih: Tasbih, isBengali: Boolean = Locale.getDefault().language == "bn"): String {
        if (!isBengali || tasbih.isCustom) return tasbih.transliteration
        return DHIKR_BN_MAP[tasbih.transliteration]?.bnTransliteration ?: tasbih.transliteration
    }

    fun getDisplayMeaning(tasbih: Tasbih, isBengali: Boolean = Locale.getDefault().language == "bn"): String {
        if (!isBengali || tasbih.isCustom) return tasbih.translatedName
        return DHIKR_BN_MAP[tasbih.transliteration]?.bnMeaning ?: tasbih.translatedName
    }
}

/**
 * Returns localized transliteration according to the current locale.
 */
fun Tasbih.getDisplayTransliteration(isBengali: Boolean = Locale.getDefault().language == "bn"): String {
    return TasbihLocalization.getDisplayTransliteration(this, isBengali)
}

/**
 * Returns localized meaning according to the current locale.
 */
fun Tasbih.getDisplayMeaning(isBengali: Boolean = Locale.getDefault().language == "bn"): String {
    return TasbihLocalization.getDisplayMeaning(this, isBengali)
}
