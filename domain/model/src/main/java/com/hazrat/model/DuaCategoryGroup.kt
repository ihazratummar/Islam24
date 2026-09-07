package com.hazrat.model

import androidx.compose.runtime.Stable

/**
 * 11 Thematic Categories mapping all 133 Hisnul Muslim chapters.
 * @author hazratummar
 */
@Stable
enum class HisnulMuslimCategory(
    val id: String,
    val title: String,
    val bnTitle: String,
    val subtitle: String,
    val bnSubtitle: String,
    val iconResName: String,
    val bannerResName: String,
    val chapterIds: List<Int>
) {
    MORNING_EVENING(
        id = "morning_evening",
        title = "Morning & Evening",
        bnTitle = "সকাল ও সন্ধ্যা",
        subtitle = "Supplications around time of day.",
        bnSubtitle = "দ্বিপ্রহর ও রাতের নিত্যদিনের দো‘আ ও যিক্‌র",
        iconResName = "ic_cat_morning_evening",
        bannerResName = "banner_morning_evening",
        chapterIds = listOf(1, 27, 28, 29, 30, 31, 32)
    ),
    PRAYER(
        id = "prayer",
        title = "Prayer",
        bnTitle = "সালাত ও ওযূ",
        subtitle = "Supplications related to Salah and Wudu.",
        bnSubtitle = "নামায ও ওযূ সংক্রান্ত দো‘আসমূহ",
        iconResName = "ic_cat_prayer",
        bannerResName = "banner_prayer",
        chapterIds = listOf(8, 9, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 33, 34)
    ),
    PRAISING_ALLAH(
        id = "praising_allah",
        title = "Praising Allah",
        bnTitle = "আল্লাহর প্রশংসা ও তাসবীহ",
        subtitle = "Glorification, Tasbih and seeking forgiveness.",
        bnSubtitle = "আল্লাহর মহিমা ঘোষণা ও ইস্তিগফার",
        iconResName = "ic_cat_praising_allah",
        bannerResName = "banner_morning_evening",
        chapterIds = listOf(108, 130, 131, 132, 133)
    ),
    HAJJ_UMRAH(
        id = "hajj_umrah",
        title = "Hajj & Umrah",
        bnTitle = "হজ্জ ও উমরা",
        subtitle = "Rites and supplications of pilgrimage.",
        bnSubtitle = "হজ্জ ও উমরার রীতিনীতি ও দো‘আসমূহ",
        iconResName = "ic_cat_hajj_umrah",
        bannerResName = "banner_prayer",
        chapterIds = listOf(116, 117, 118, 119, 120, 121, 122, 128)
    ),
    TRAVEL(
        id = "travel",
        title = "Travel",
        bnTitle = "সফর ও ভ্রমণ",
        subtitle = "Duas for journey, vehicles, and destinations.",
        bnSubtitle = "যানবাহন ও যাত্রার দো‘আসমূহ",
        iconResName = "ic_cat_travel",
        bannerResName = "banner_morning_evening",
        chapterIds = listOf(96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106)
    ),
    JOY_DISTRESS(
        id = "joy_distress",
        title = "Joy & Distress",
        bnTitle = "আনন্দ ও বিপদে",
        subtitle = "Relief from anxiety, sorrow, and difficult affairs.",
        bnSubtitle = "দুশ্চিন্তা, ভয় ও বিপদমুক্তির দো‘আ",
        iconResName = "ic_cat_joy_distress",
        bannerResName = "banner_morning_evening",
        chapterIds = listOf(35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 83, 84, 92, 107, 123, 124, 126, 127, 129)
    ),
    NATURE(
        id = "nature",
        title = "Nature",
        bnTitle = "প্রাকৃতিক নিদর্শন",
        subtitle = "Wind, rain, thunder, and natural wonders.",
        bnSubtitle = "বৃষ্টি, ঝড়, বজ্রপাত ও প্রকৃতির দো‘আ",
        iconResName = "ic_cat_nature",
        bannerResName = "banner_morning_evening",
        chapterIds = listOf(62, 63, 64, 65, 66, 67, 68, 77, 111, 112)
    ),
    GOOD_ETIQUETTE(
        id = "good_etiquette",
        title = "Good Etiquette",
        bnTitle = "উত্তম শিষ্টাচার",
        subtitle = "Social manners, greetings, and character.",
        bnSubtitle = "সালাম, পারস্পরিক ব্যবহার ও শিষ্টাচার",
        iconResName = "ic_cat_good_etiquette",
        bannerResName = "banner_prayer",
        chapterIds = listOf(78, 79, 85, 86, 87, 88, 89, 90, 91, 93, 94, 95, 109, 110, 113, 114, 115)
    ),
    HOME_FAMILY(
        id = "home_family",
        title = "Home & Family",
        bnTitle = "ঘর ও পরিবার",
        subtitle = "Entering, leaving home, clothes, and family life.",
        bnSubtitle = "ঘরে প্রবেশ, পোশাক ও পারিবারিক জীবন",
        iconResName = "ic_cat_home_family",
        bannerResName = "banner_prayer",
        chapterIds = listOf(2, 3, 4, 5, 6, 7, 10, 11, 48, 49, 80, 81, 82)
    ),
    FOOD_DRINK(
        id = "food_drink",
        title = "Food & Drink",
        bnTitle = "পানাহার ও আতিথেয়তা",
        subtitle = "Supplications for meals, guests, and fasting.",
        bnSubtitle = "খাবার, মেহমানদারি ও রোজার দো‘আ",
        iconResName = "ic_cat_food_drink",
        bannerResName = "banner_morning_evening",
        chapterIds = listOf(69, 70, 71, 72, 73, 74, 75, 76)
    ),
    SICKNESS_DEATH(
        id = "sickness_death",
        title = "Sickness & Death",
        bnTitle = "অসুস্থতা ও মৃত্যু",
        subtitle = "Healing, visiting the sick, funerals, and condolences.",
        bnSubtitle = "রোগমুক্তি, রোগী দর্শন ও জানাযার দো‘আ",
        iconResName = "ic_cat_sickness_death",
        bannerResName = "banner_prayer",
        chapterIds = listOf(50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 125)
    );

    companion object {
        fun fromId(id: String): HisnulMuslimCategory {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: MORNING_EVENING
        }
    }
}
