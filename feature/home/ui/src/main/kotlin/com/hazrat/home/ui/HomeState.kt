package com.hazrat.home.ui

import com.hazrat.model.IslamicEventsInfoModel
import com.hazrat.model.MinimalPrayerData
import com.hazrat.usecase.UpcomingIslamicEvent

data class DailyVerseData(
    val surahName: String = "Al-Ankabut",
    val verseNumber: Int = 45,
    val arabicText: String = "وَأَقِمِ الصَّلَاةَ لِذِكْرِي وَإِنَّ الصَّلَاةَ تَنْهَىٰ عَنِ الْفَحْشَاءِ وَالْمُنْكَرِ",
    val englishTranslation: String = "And establish prayer for My remembrance. Indeed, prayer prohibits immorality and wrongdoing."
)

data class DailyDuaData(
    val categoryTitle: String = "Upon Waking Up",
    val arabicText: String = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
    val transliteration: String = "Alhamdu lillahi alladhi ahyana ba'da ma amatana wa ilayhin-nushur.",
    val translation: String = "All praise is for Allah who gave us life after having taken it from us and to Him is the resurrection.",
    val reference: String = "Sahih al-Bukhari"
)

data class WeeklyPrayerStats(
    val dailyCounts: List<Int> = listOf(5, 4, 5, 3, 5, 5, 4), // Sun -> Sat
    val currentDayIndex: Int = 6, // 0 = Sun, 6 = Sat
    val totalCompleted: Int = 32,
    val totalTarget: Int = 35
)

data class HomeState(
    val dailyQuranDate: String = "",
    val randomAyatNumber: Int = 0,
    val prayerData: MinimalPrayerData = MinimalPrayerData(),
    val upcomingIslamicEvent: UpcomingIslamicEvent? = null,
    val islamicEventsInfoModel: List<IslamicEventsInfoModel?> = emptyList(),
    val fridayTime: Long? = null,
    val isLocationLoading: Boolean = false,
    val isPrayerTimeLoading: Boolean = false,
    val dailyVerse: DailyVerseData = DailyVerseData(),
    val dailyDua: DailyDuaData = DailyDuaData(),
    val weeklyPrayerStats: WeeklyPrayerStats = WeeklyPrayerStats()
)
