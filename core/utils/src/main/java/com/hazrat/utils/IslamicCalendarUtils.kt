package com.hazrat.utils

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.util.Calendar
import java.util.Locale


data class HijriDateInfo(
    val day: Int,
    val monthName: String,
    val year: Int
)

object IslamicCalendarUtils  {

    fun getNextRamadan() : UmmalquraCalendar {
        val current = UmmalquraCalendar()

        val currentHijriYear = current.get(UmmalquraCalendar.YEAR)

        val currentHijriMonth = current.get(UmmalquraCalendar.MONTH) + 1

        val ramadanYear = if (currentHijriMonth >= 9){
            currentHijriYear + 1
        }else {
            currentHijriYear
        }
        return UmmalquraCalendar().apply {
            set(ramadanYear, 8, 1)
        }
    }

    private val bengaliHijriMonths = listOf(
        "মুহাররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
        "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান",
        "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ্জ"
    )

    private val englishHijriMonths = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Ula", "Jumada al-Akhirah", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    fun getCurrentHijriDateInfo(locale: Locale = Locale.getDefault()): HijriDateInfo {
        val calendar = UmmalquraCalendar()
        val monthIndex = calendar.get(UmmalquraCalendar.MONTH)

        val monthName = if (locale.language == "bn") {
            bengaliHijriMonths.getOrElse(monthIndex) { "" }
        } else {
            calendar.getDisplayName(
                UmmalquraCalendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH
            ) ?: englishHijriMonths.getOrElse(monthIndex) { "" }
        }

        return HijriDateInfo(
            day = calendar.get(UmmalquraCalendar.DAY_OF_MONTH),
            monthName = monthName,
            year = calendar.get(UmmalquraCalendar.YEAR)
        )
    }

    fun getLocalizedHijriMonth(monthNameEn: String, locale: Locale = Locale.getDefault()): String {
        if (locale.language != "bn") return monthNameEn
        val normalized = monthNameEn.lowercase()
        return when {
            normalized.contains("muharram") -> "মুহাররম"
            normalized.contains("safar") -> "সফর"
            normalized.contains("rabi") && (normalized.contains("awwal") || normalized.contains("1") || normalized.contains("first")) -> "রবিউল আউয়াল"
            normalized.contains("rabi") && (normalized.contains("thani") || normalized.contains("akhira") || normalized.contains("2") || normalized.contains("second")) -> "রবিউস সানি"
            normalized.contains("jumad") && (normalized.contains("awwal") || normalized.contains("ula") || normalized.contains("1") || normalized.contains("first")) -> "জমাদিউল আউয়াল"
            normalized.contains("jumad") && (normalized.contains("thani") || normalized.contains("akhir") || normalized.contains("2") || normalized.contains("second")) -> "জমাদিউস সানি"
            normalized.contains("rajab") -> "রজব"
            normalized.contains("sha") && (normalized.contains("ban") || normalized.contains("ba")) -> "শাবান"
            normalized.contains("ramad") -> "রমজান"
            normalized.contains("shawwal") -> "শাওয়াল"
            normalized.contains("qi") || normalized.contains("qa") -> "জিলকদ"
            normalized.contains("hij") -> "জিলহজ্জ"
            else -> monthNameEn
        }
    }

    /**
     * Map fixed Islamic events and scholar Urs names from the database into localized strings.
     */
    fun getLocalizedEventName(rawEventName: String, locale: Locale = Locale.getDefault()): String {
        if (locale.language != "bn") return rawEventName
        val trimmed = rawEventName.trim()

        // 1. Direct exact/normalized mapping for major events
        val exactMatch = majorEventsMapBn[trimmed.lowercase()]
        if (exactMatch != null) return exactMatch

        // 2. Exact scholar / saint Urs mapping
        val saintMatch = saintsUrsMapBn[trimmed.lowercase()]
        if (saintMatch != null) return saintMatch

        // 3. Dynamic Urs of ... mapping with name token translation
        if (trimmed.startsWith("Urs of ", ignoreCase = true) || trimmed.startsWith("Urs: ", ignoreCase = true)) {
            val rawName = if (trimmed.startsWith("Urs of ", ignoreCase = true)) {
                trimmed.substring(7).trim()
            } else {
                trimmed.substring(5).trim()
            }
            val localizedScholar = translateScholarNameBn(rawName)
            return "উরস: $localizedScholar"
        }

        // 4. Fallback pattern matching for common keywords
        val lower = trimmed.lowercase()
        return when {
            lower.contains("ashura") -> "পবিত্র আশুরা"
            lower.contains("arbaeen") || lower.contains("arba'een") -> "চেহলাম (আরবাঈন)"
            lower.contains("mawlid") || lower.contains("milad") -> "পবিত্র ঈদে মিলাদুন্নবী (সা.)"
            lower.contains("miraj") || lower.contains("mi'raj") -> "পবিত্র শবে মেরাজ"
            lower.contains("bara'at") || lower.contains("barat") || lower.contains("shab-e-barat") -> "পবিত্র শবে বরাত"
            lower.contains("qadr") || lower.contains("shab-e-qadr") -> "পবিত্র শবে কদর"
            lower.contains("ramadan") && (lower.contains("1") || lower.contains("first") || lower.contains("start")) -> "১ম রমজান"
            lower.contains("chaand raat") || lower.contains("chand raat") -> "চাঁদ রাত"
            lower.contains("eid al-fitr") || lower.contains("eid-ul-fitr") || lower.contains("eid ul fitr") -> "পবিত্র ঈদুল ফিতর"
            lower.contains("arafah") || lower.contains("arafat") -> "আরাফার দিন (হজ্জ)"
            lower.contains("eid al-adha") || lower.contains("eid-ul-adha") || lower.contains("eid ul adha") -> "পবিত্র ঈদুল আযহা"
            lower.contains("tashreeq") || lower.contains("tashriq") -> "আইয়ামে তাশরিক"
            lower.contains("islamic new year") || lower.contains("hijri new year") || lower.contains("1st muharram") -> "হিজরি নববর্ষ"
            lower.contains("jummah") || lower.contains("jumu'ah") || lower.contains("jumuah") -> "জুমু'আ"
            else -> trimmed
        }
    }

    private fun translateScholarNameBn(name: String): String {
        // Direct known scholar name lookup
        val direct = knownScholarsBn[name.lowercase()]
        if (direct != null) return direct

        // Token-by-token phonetic replacement
        var result = name
        scholarTokensBn.forEach { (en, bn) ->
            result = result.replace(Regex("(?i)\\b$en\\b"), bn)
        }
        // Replace prefixes like al-, as-, etc.
        result = result
            .replace(Regex("(?i)\\bal-"), "আল-")
            .replace(Regex("(?i)\\bas-"), "আস-")
            .replace(Regex("(?i)\\ban-"), "আন-")
            .replace(Regex("(?i)\\bar-"), "আর-")
            .replace(Regex("(?i)\\bash-"), "আশ-")
            .replace(Regex("(?i)\\baz-"), "আজ-")
            .replace(Regex("(?i)\\bat-"), "আত-")
            .replace(Regex("(?i)\\bad-"), "আদ-")
            .replace(Regex("(?i)\\badh-"), "আদ-")

        return result
    }

    fun getLocalizedGregorianMonth(monthNameEn: String, locale: Locale = Locale.getDefault()): String {
        if (locale.language != "bn") return monthNameEn
        val normalized = monthNameEn.lowercase()
        return when {
            normalized.contains("jan") -> "জানুয়ারি"
            normalized.contains("feb") -> "ফেব্রুয়ারি"
            normalized.contains("mar") -> "মার্চ"
            normalized.contains("apr") -> "এপ্রিল"
            normalized.contains("may") -> "মে"
            normalized.contains("jun") -> "জুন"
            normalized.contains("jul") -> "জুলাই"
            normalized.contains("aug") -> "আগস্ট"
            normalized.contains("sep") -> "সেপ্টেম্বর"
            normalized.contains("oct") -> "অক্টোবর"
            normalized.contains("nov") -> "নভেম্বর"
            normalized.contains("dec") -> "ডিসেম্বর"
            else -> monthNameEn
        }
    }

    private val majorEventsMapBn = mapOf(
        "sunnah fasting" to "সুন্নাহ রোজা",
        "fasting" to "সুন্নাত রোজা",
        "12 rabi' al-awwal" to "১২ রবিউল আউয়াল",
        "12 rabi al awwal" to "১২ রবিউল আউয়াল",
        "islamic new year" to "হিজরি নববর্ষ",
        "1st muharram" to "হিজরি নববর্ষ (১ম মুহাররম)",
        "day of ashura" to "পবিত্র আশুরা",
        "ashura" to "পবিত্র আশুরা",
        "arbaeen" to "চেহলাম (আরবাঈন)",
        "arba'een" to "চেহলাম (আরবাঈন)",
        "mawlid an-nabi" to "পবিত্র ঈদে মিলাদুন্নবী (সা.)",
        "mawlid al-nabi" to "পবিত্র ঈদে মিলাদুন্নবী (সা.)",
        "milad un-nabi" to "পবিত্র ঈদে মিলাদুন্নবী (সা.)",
        "eid milad un nabi" to "পবিত্র ঈদে মিলাদুন্নবী (সা.)",
        "birthday of the prophet" to "পবিত্র ঈদে মিলাদুন্নবী (সা.)",
        "laylat al-miraj" to "পবিত্র শবে মেরাজ",
        "isra and mi'raj" to "পবিত্র শবে মেরাজ",
        "isra and miraj" to "পবিত্র শবে মেরাজ",
        "isra and mi’raj" to "পবিত্র শবে মেরাজ",
        "shab-e-miraj" to "পবিত্র শবে মেরাজ",
        "laylat al-bara'at" to "পবিত্র শবে বরাত",
        "laylat al-baraat" to "পবিত্র শবে বরাত",
        "mid-sha'ban" to "পবিত্র শবে বরাত (১৫ই শাবান)",
        "shab-e-barat" to "পবিত্র শবে বরাত",
        "1st day of ramadan" to "১ম রমজান (পবিত্র মাহে রমজান)",
        "first day of ramadan" to "১ম রমজান (পবিত্র মাহে রমজান)",
        "laylat al-qadr" to "পবিত্র শবে কদর",
        "shab-e-qadr" to "পবিত্র শবে কদর",
        "chaand raat" to "চাঁদ রাত",
        "eid al-fitr" to "পবিত্র ঈদুল ফিতর",
        "eid-ul-fitr" to "পবিত্র ঈদুল ফিতর",
        "day of arafah" to "আরাফার দিন (হজ্জের দিন)",
        "arafah" to "আরাফার দিন",
        "eid al-adha" to "পবিত্র ঈদুল আযহা",
        "eid-ul-adha" to "পবিত্র ঈদুল আযহা",
        "days of tashreeq" to "আইয়ামে তাশরিক",
        "jummah" to "জুমু'আ",
        "jumu'ah" to "জুমু'আ"
    )

    private val saintsUrsMapBn = mapOf(
        "urs of shaykh mahmūd al-anjir" to "উরস: শায়খ মাহমুদ আল-আনজির",
        "urs of shaykh mahmud al-anjir" to "উরস: শায়খ মাহমুদ আল-আনজির",
        "urs of shaykh abu ahmad as-sughūri" to "উরস: শায়খ আবু আহমদ আস-সুঘুরী",
        "urs of shaykh abu ahmad as-sughuri" to "উরস: শায়খ আবু আহমদ আস-সুঘুরী",
        "urs of shaykh jamaluddin al-ghumuqi" to "উরস: শায়খ জামালউদ্দিন আল-গুমুকি",
        "urs of shaykh khas muhammad ash-shirwani" to "উরস: শায়খ খাস মুহাম্মদ আশ-শিরওয়ানী",
        "urs of shaykh ismail ash-shirwani" to "উরস: শায়খ ইসমাইল আশ-শিরওয়ানী",
        "urs of shaykh khalid al-baghdadi" to "উরস: শায়খ খালিদ আল-বাগদাদী",
        "urs of shah ghulam ali dehlavi" to "উরস: শাহ গোলাম আলী দেহলবী",
        "urs of mirza mazhar jan-e-janan" to "উরস: মির্জা মাজহার জানে জানান",
        "urs of shaykh nur muhammad al-badayuni" to "উরস: শায়খ নূর মুহাম্মদ আল-বাদায়ুনী",
        "urs of shaykh sayfuddin al-faruqi" to "উরস: শায়খ সাইফুদ্দীন আল-ফারুকী",
        "urs of shaykh muhammad masum" to "উরস: শায়খ মুহাম্মদ মাসুম ফারুকী",
        "urs of shaykh ahmad al-faruqi sirhindi" to "উরস: মুজাদ্দিদে আলফে সানী (রহ.)",
        "urs of imam rabbani" to "উরস: ইমাম রব্বানী মুজাদ্দিদে আলফে সানী (রহ.)",
        "urs of khwaja baqi billah" to "উরস: খাজা বাকী বিল্লাহ (রহ.)",
        "urs of khwaja muhammad bahauddin naqshband" to "উরস: খাজা বাহাউদ্দীন নকশবন্দ (রহ.)",
        "urs of shah naqshband" to "উরস: শাহ নকশবন্দ (রহ.)",
        "urs of khwaja moinuddin chishti" to "উরস: খাজা মঈনুদ্দীন চিশতী (রহ.)",
        "urs of khwaja nizamuddin auliya" to "উরস: হযরত খাজা নিজামুদ্দিন আউলিয়া (রহ.)",
        "urs of shaykh abdul qadir jilani" to "উরস: বড়পীর আব্দুল কাদের জিলানী (রহ.)",
        "urs of shaykh abdul qadir gilani" to "উরস: বড়পীর আব্দুল কাদের জিলানী (রহ.)",
        "urs of lal shahbaz qalandar" to "উরস: হযরত লাল শাহবাজ কলন্দর (রহ.)",
        "urs of hazrat shah jalal" to "উরস: হযরত শাহজালাল (রহ.)",
        "urs of shah jalal" to "উরস: হযরত শাহজালাল (রহ.)",
        "urs of hazrat shah paran" to "উরস: হযরত শাহ পরান (রহ.)"
    )

    private val knownScholarsBn = mapOf(
        "shaykh mahmūd al-anjir" to "শায়খ মাহমুদ আল-আনজির",
        "shaykh mahmud al-anjir" to "শায়খ মাহমুদ আল-আনজির",
        "shaykh abu ahmad as-sughūri" to "শায়খ আবু আহমদ আস-সুঘুরী",
        "shaykh abu ahmad as-sughuri" to "শায়খ আবু আহমদ আস-সুঘুরী",
        "shaykh jamaluddin al-ghumuqi" to "শায়খ জামালউদ্দিন আল-গুমুকি",
        "shaykh khas muhammad ash-shirwani" to "শায়খ খাস মুহাম্মদ আশ-শিরওয়ানী",
        "shaykh ismail ash-shirwani" to "শায়খ ইসমাইল আশ-শিরওয়ানী",
        "shaykh khalid al-baghdadi" to "শায়খ খালিদ আল-বাগদাদী",
        "shah ghulam ali dehlavi" to "শাহ গোলাম আলী দেহলবী",
        "mirza mazhar jan-e-janan" to "মির্জা মাজহার জানে জানান",
        "shaykh ahmad al-faruqi sirhindi" to "মুজাদ্দিদে আলফে সানী (রহ.)",
        "khwaja baqi billah" to "খাজা বাকী বিল্লাহ (রহ.)",
        "khwaja moinuddin chishti" to "খাজা মঈনুদ্দীন চিশতী (রহ.)",
        "khwaja nizamuddin auliya" to "খাজা নিজামুদ্দিন আউলিয়া (রহ.)",
        "shaykh abdul qadir jilani" to "বড়পীর আব্দুল কাদের জিলানী (রহ.)",
        "shaykh abdul qadir gilani" to "বড়পীর আব্দুল কাদের জিলানী (রহ.)",
        "shah jalal" to "শাহজালাল (রহ.)",
        "hazrat shah jalal" to "হযরত শাহজালাল (রহ.)",
        "shah paran" to "শাহ পরান (রহ.)",
        "hazrat shah paran" to "হযরত শাহ পরান (রহ.)"
    )

    private val scholarTokensBn = listOf(
        "shaykh" to "শায়খ",
        "sheikh" to "শায়খ",
        "imam" to "ইমাম",
        "khwaja" to "খাজা",
        "sayyid" to "সৈয়দ",
        "syed" to "সৈয়দ",
        "hazrat" to "হযরত",
        "pir" to "পীর",
        "shah" to "শাহ",
        "sultan" to "সুলতান",
        "mirza" to "মির্জা",
        "abu" to "আবু",
        "ibn" to "ইবনে",
        "bin" to "বিন",
        "ahmad" to "আহমদ",
        "ahmed" to "আহমদ",
        "muhammad" to "মুহাম্মদ",
        "mohammed" to "মুহাম্মদ",
        "mahmud" to "মাহমুদ",
        "mahmūd" to "মাহমুদ",
        "ali" to "আলী",
        "hasan" to "হাসান",
        "hassan" to "হাসান",
        "husayn" to "হুসাইন",
        "hussain" to "হুসাইন",
        "umar" to "উমর",
        "omar" to "উমর",
        "uthman" to "উসমান",
        "usman" to "উসমান",
        "bakr" to "বকর",
        "anjir" to "আনজির",
        "sughuri" to "সুঘুরী",
        "sughūri" to "সুঘুরী",
        "ghumuqi" to "গুমুকি",
        "shirwani" to "শিরওয়ানী",
        "baghdadi" to "বাগদাদী",
        "dehlavi" to "দেহলবী",
        "sirhindi" to "সিরহিন্দী"
    )
}