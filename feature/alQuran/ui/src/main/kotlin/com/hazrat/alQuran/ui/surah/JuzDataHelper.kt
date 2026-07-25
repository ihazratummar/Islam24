package com.hazrat.alQuran.ui.surah

data class JuzSurahSegment(
    val surahNumber: Int,
    val surahNameEnglish: String,
    val surahNameArabic: String,
    val startAyah: Int,
    val endAyah: Int
)

data class JuzItem(
    val juzNumber: Int,
    val segments: List<JuzSurahSegment>
)

object JuzDataHelper {
    fun getJuzList(): List<JuzItem> {
        return listOf(
            JuzItem(1, listOf(
                JuzSurahSegment(1, "Al-Faatiha", "الفاتحة", 1, 7),
                JuzSurahSegment(2, "Al-Baqara", "البقرة", 1, 141)
            )),
            JuzItem(2, listOf(
                JuzSurahSegment(2, "Al-Baqara", "البقرة", 142, 252)
            )),
            JuzItem(3, listOf(
                JuzSurahSegment(2, "Al-Baqara", "البقرة", 253, 286),
                JuzSurahSegment(3, "Aal-i-Imraan", "آل عمران", 1, 92)
            )),
            JuzItem(4, listOf(
                JuzSurahSegment(3, "Aal-i-Imraan", "آل عمران", 93, 200),
                JuzSurahSegment(4, "An-Nisaa", "النساء", 1, 23)
            )),
            JuzItem(5, listOf(
                JuzSurahSegment(4, "An-Nisaa", "النساء", 24, 147)
            )),
            JuzItem(6, listOf(
                JuzSurahSegment(4, "An-Nisaa", "النساء", 148, 176),
                JuzSurahSegment(5, "Al-Maaida", "المائدة", 1, 81)
            )),
            JuzItem(7, listOf(
                JuzSurahSegment(5, "Al-Maaida", "المائدة", 82, 120),
                JuzSurahSegment(6, "Al-An'aam", "الأنعام", 1, 110)
            )),
            JuzItem(8, listOf(
                JuzSurahSegment(6, "Al-An'aam", "الأنعام", 111, 165),
                JuzSurahSegment(7, "Al-A'raaf", "الأعراف", 1, 87)
            )),
            JuzItem(9, listOf(
                JuzSurahSegment(7, "Al-A'raaf", "الأعراف", 88, 206),
                JuzSurahSegment(8, "Al-Anfaal", "الأنفال", 1, 40)
            )),
            JuzItem(10, listOf(
                JuzSurahSegment(8, "Al-Anfaal", "الأنفال", 41, 75),
                JuzSurahSegment(9, "At-Tawba", "التوبة", 1, 92)
            )),
            JuzItem(11, listOf(
                JuzSurahSegment(9, "At-Tawba", "التوبة", 93, 129),
                JuzSurahSegment(10, "Yunus", "يونس", 1, 109),
                JuzSurahSegment(11, "Hud", "هود", 1, 5)
            )),
            JuzItem(12, listOf(
                JuzSurahSegment(11, "Hud", "هود", 6, 123),
                JuzSurahSegment(12, "Yusuf", "يوسف", 1, 52)
            )),
            JuzItem(13, listOf(
                JuzSurahSegment(12, "Yusuf", "يوسف", 53, 111),
                JuzSurahSegment(13, "Ar-Ra'd", "الرعد", 1, 43),
                JuzSurahSegment(14, "Ibrahim", "ابراهيم", 1, 52)
            )),
            JuzItem(14, listOf(
                JuzSurahSegment(15, "Al-Hijr", "الحجر", 1, 99),
                JuzSurahSegment(16, "An-Nahl", "النحل", 1, 128)
            )),
            JuzItem(15, listOf(
                JuzSurahSegment(17, "Al-Israa", "الإسراء", 1, 111),
                JuzSurahSegment(18, "Al-Kahf", "الكهف", 1, 74)
            )),
            JuzItem(16, listOf(
                JuzSurahSegment(18, "Al-Kahf", "الكهف", 75, 110),
                JuzSurahSegment(19, "Maryam", "مريم", 1, 98),
                JuzSurahSegment(20, "Taa-Haa", "طه", 1, 135)
            )),
            JuzItem(17, listOf(
                JuzSurahSegment(21, "Al-Anbiyaa", "الأنبياء", 1, 112),
                JuzSurahSegment(22, "Al-Hajj", "الحج", 1, 78)
            )),
            JuzItem(18, listOf(
                JuzSurahSegment(23, "Al-Mu'minoon", "المؤمنون", 1, 118),
                JuzSurahSegment(24, "An-Noor", "النور", 1, 64),
                JuzSurahSegment(25, "Al-Furqaan", "الفرقان", 1, 20)
            )),
            JuzItem(19, listOf(
                JuzSurahSegment(25, "Al-Furqaan", "الفرقان", 21, 77),
                JuzSurahSegment(26, "Ash-Shu'araa", "الشعراء", 1, 227),
                JuzSurahSegment(27, "An-Naml", "النمل", 1, 55)
            )),
            JuzItem(20, listOf(
                JuzSurahSegment(27, "An-Naml", "النمل", 56, 93),
                JuzSurahSegment(28, "Al-Qasas", "القصص", 1, 88),
                JuzSurahSegment(29, "Al-Ankaboot", "العنكبوت", 1, 45)
            )),
            JuzItem(21, listOf(
                JuzSurahSegment(29, "Al-Ankaboot", "العنكبوت", 46, 69),
                JuzSurahSegment(30, "Ar-Room", "الروم", 1, 60),
                JuzSurahSegment(31, "Luqman", "لقمان", 1, 34),
                JuzSurahSegment(32, "As-Sajda", "السجدة", 1, 30),
                JuzSurahSegment(33, "Al-Ahzaab", "الأحزاب", 1, 30)
            )),
            JuzItem(22, listOf(
                JuzSurahSegment(33, "Al-Ahzaab", "الأحزاب", 31, 73),
                JuzSurahSegment(34, "Saba", "سبإ", 1, 54),
                JuzSurahSegment(35, "Faatir", "فاطر", 1, 45),
                JuzSurahSegment(36, "Yaseen", "يس", 1, 27)
            )),
            JuzItem(23, listOf(
                JuzSurahSegment(36, "Yaseen", "يس", 28, 83),
                JuzSurahSegment(37, "As-Saaffaat", "الصافات", 1, 182),
                JuzSurahSegment(38, "Saad", "ص", 1, 88),
                JuzSurahSegment(39, "Az-Zumar", "الزمر", 1, 31)
            )),
            JuzItem(24, listOf(
                JuzSurahSegment(39, "Az-Zumar", "الزمر", 32, 75),
                JuzSurahSegment(40, "Ghafir", "غافر", 1, 85),
                JuzSurahSegment(41, "Fussilat", "فصلت", 1, 46)
            )),
            JuzItem(25, listOf(
                JuzSurahSegment(41, "Fussilat", "فصلت", 47, 54),
                JuzSurahSegment(42, "Ash-Shura", "الشورى", 1, 53),
                JuzSurahSegment(43, "Az-Zukhruf", "الزخرف", 1, 89),
                JuzSurahSegment(44, "Ad-Dukhaan", "الدخان", 1, 59),
                JuzSurahSegment(45, "Al-Jaathiya", "الجاثية", 1, 37)
            )),
            JuzItem(26, listOf(
                JuzSurahSegment(46, "Al-Ahqaf", "الأحقاف", 1, 35),
                JuzSurahSegment(47, "Muhammad", "محمد", 1, 38),
                JuzSurahSegment(48, "Al-Fath", "الفتح", 1, 29),
                JuzSurahSegment(49, "Al-Hujuraat", "الحجرات", 1, 18),
                JuzSurahSegment(50, "Qaf", "ق", 1, 45),
                JuzSurahSegment(51, "Adh-Dhaariyat", "الذاريات", 1, 30)
            )),
            JuzItem(27, listOf(
                JuzSurahSegment(51, "Adh-Dhaariyat", "الذاريات", 31, 60),
                JuzSurahSegment(52, "At-Toor", "الطور", 1, 49),
                JuzSurahSegment(53, "An-Najm", "النجم", 1, 62),
                JuzSurahSegment(54, "Al-Qamar", "القمر", 1, 55),
                JuzSurahSegment(55, "Ar-Rahmaan", "الرحمن", 1, 78),
                JuzSurahSegment(56, "Al-Waaqia", "الواقعة", 1, 96),
                JuzSurahSegment(57, "Al-Hadeed", "الحديد", 1, 29)
            )),
            JuzItem(28, listOf(
                JuzSurahSegment(58, "Al-Mujaadila", "المجادلة", 1, 22),
                JuzSurahSegment(59, "Al-Hashr", "الحشر", 1, 24),
                JuzSurahSegment(60, "Al-Mumtahana", "الممتحنة", 1, 13),
                JuzSurahSegment(61, "As-Saff", "الصف", 1, 14),
                JuzSurahSegment(62, "Al-Jumu'a", "الجمعة", 1, 11),
                JuzSurahSegment(63, "Al-Munaafiqoon", "المنافقون", 1, 11),
                JuzSurahSegment(64, "At-Taghabun", "التغابن", 1, 18),
                JuzSurahSegment(65, "At-Talaaq", "الطلاق", 1, 12),
                JuzSurahSegment(66, "At-Tahreem", "التحريم", 1, 12)
            )),
            JuzItem(29, listOf(
                JuzSurahSegment(67, "Al-Mulk", "الملك", 1, 30),
                JuzSurahSegment(68, "Al-Qalam", "القلم", 1, 52),
                JuzSurahSegment(69, "Al-Haaqqa", "الحاقة", 1, 52),
                JuzSurahSegment(70, "Al-Ma'aarij", "المعارج", 1, 44),
                JuzSurahSegment(71, "Nooh", "نوح", 1, 28),
                JuzSurahSegment(72, "Al-Jinn", "الجن", 1, 28),
                JuzSurahSegment(73, "Al-Muzzammil", "المزمل", 1, 20),
                JuzSurahSegment(74, "Al-Muddaththir", "المدثر", 1, 56),
                JuzSurahSegment(75, "Al-Qiyaama", "القيامة", 1, 40),
                JuzSurahSegment(76, "Al-Insaan", "الانسان", 1, 31),
                JuzSurahSegment(77, "Al-Mursalaat", "المرسلات", 1, 50)
            )),
            JuzItem(30, listOf(
                JuzSurahSegment(78, "An-Naba", "النبإ", 1, 40),
                JuzSurahSegment(79, "An-Naazi'aat", "النازعات", 1, 46),
                JuzSurahSegment(80, "Abasa", "عبس", 1, 42),
                JuzSurahSegment(81, "At-Takweer", "التكوير", 1, 29),
                JuzSurahSegment(82, "Al-Infitaar", "الإنفطار", 1, 19),
                JuzSurahSegment(83, "Al-Mutaffifeen", "المطففين", 1, 36),
                JuzSurahSegment(84, "Al-Inshiqaaq", "الإنشقاق", 1, 25),
                JuzSurahSegment(85, "Al-Burooj", "البروج", 1, 22),
                JuzSurahSegment(86, "At-Taariq", "الطارق", 1, 17),
                JuzSurahSegment(87, "Al-A'la", "الأعلى", 1, 19),
                JuzSurahSegment(88, "Al-Ghaashiya", "الغاشية", 1, 26),
                JuzSurahSegment(89, "Al-Fajr", "الفجر", 1, 30),
                JuzSurahSegment(90, "Al-Balad", "البلد", 1, 20),
                JuzSurahSegment(91, "Ash-Shams", "الشمس", 1, 15),
                JuzSurahSegment(92, "Al-Lail", "الليل", 1, 21),
                JuzSurahSegment(93, "Ad-Dhuhaa", "الضحى", 1, 11),
                JuzSurahSegment(94, "Ash-Sharh", "الشرح", 1, 8),
                JuzSurahSegment(95, "At-Teen", "التين", 1, 8),
                JuzSurahSegment(96, "Al-Alaq", "العلق", 1, 19),
                JuzSurahSegment(97, "Al-Qadr", "القدر", 1, 5),
                JuzSurahSegment(98, "Al-Bayyina", "البينة", 1, 8),
                JuzSurahSegment(99, "Az-Zalzala", "الزلزلة", 1, 8),
                JuzSurahSegment(100, "Al-Aadiyaat", "العاديات", 1, 11),
                JuzSurahSegment(101, "Al-Qaari'a", "القارعة", 1, 11),
                JuzSurahSegment(102, "At-Takaathur", "التكاثر", 1, 8),
                JuzSurahSegment(103, "Al-Asr", "العصر", 1, 3),
                JuzSurahSegment(104, "Al-Humaza", "الهمزة", 1, 9),
                JuzSurahSegment(105, "Al-Feel", "الفيل", 1, 5),
                JuzSurahSegment(106, "Quraysh", "قريش", 1, 4),
                JuzSurahSegment(107, "Al-Maa'un", "الماعون", 1, 7),
                JuzSurahSegment(108, "Al-Kawthar", "الكوثر", 1, 3),
                JuzSurahSegment(109, "Al-Kaafiroon", "الكافرون", 1, 6),
                JuzSurahSegment(110, "An-Nasr", "النصر", 1, 3),
                JuzSurahSegment(111, "Al-Masad", "المسد", 1, 5),
                JuzSurahSegment(112, "Al-Ikhlaas", "الإخلاص", 1, 4),
                JuzSurahSegment(113, "Al-Falaq", "الفلق", 1, 5),
                JuzSurahSegment(114, "An-Naas", "الناس", 1, 6)
            ))
        )
    }
}
