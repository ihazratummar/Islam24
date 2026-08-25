package com.hazrat.islam24.widget.hijridate

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.hazrat.database.dao.prayer.PrayerTimeDao
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.islam24.main.navigation.NavigationCommandBus
import com.hazrat.islam24.main.navigation.NavigationTarget
import com.hazrat.ui.R
import com.hazrat.utils.DateUtil
import org.koin.core.context.GlobalContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HijriDateWidget : GlanceAppWidget() {

    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 80.dp)
        private val MEDIUM_RECT = DpSize(200.dp, 80.dp)
        private val LARGE_RECT = DpSize(320.dp, 100.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(SMALL_SQUARE, MEDIUM_RECT, LARGE_RECT)
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val hijriInfo = loadHijriDateInfo(context)

        provideContent {
            val size = LocalSize.current
            val isCompact = size.width < 180.dp

            val calendarAction = actionRunCallback<OpenCalendarActionCallback>()

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ImageProvider(R.drawable.bg_hijri_date_widget))
                    .clickable(calendarAction)
                    .padding(
                        horizontal = if (isCompact) 12.dp else 16.dp,
                        vertical = if (isCompact) 10.dp else 14.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompact) {
                    // ── Compact Mode (2x2 square): Day Number occupies ~70% of widget ──
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.Vertical.CenterVertically
                    ) {
                        // Top: Compact Tag
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            Image(
                                provider = ImageProvider(R.drawable.ic_calendar_gold),
                                contentDescription = null,
                                modifier = GlanceModifier.size(11.dp)
                            )
                            Spacer(modifier = GlanceModifier.width(4.dp))
                            Text(
                                text = "HIJRI DATE",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = ColorProvider(Color(0xFFFCD34D)),
                                    fontSize = 9.sp
                                )
                            )
                        }

                        Spacer(modifier = GlanceModifier.defaultWeight())

                        // Center: 70% visual prominence Day Number
                        Text(
                            text = hijriInfo.hijriDay,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = ColorProvider(Color(0xFFFEF3C7)),
                                fontSize = 54.sp
                            )
                        )

                        Spacer(modifier = GlanceModifier.defaultWeight())

                        // Bottom: Month & Year
                        Text(
                            text = "${hijriInfo.hijriMonth} ${hijriInfo.hijriYear} AH",
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                color = ColorProvider(Color(0xFFFBBF24)),
                                fontSize = 12.sp
                            ),
                            maxLines = 1
                        )
                    }
                } else {
                    // ── Full Standard / Expanded Wide Mode: Exact 1:1 Pixel Match ──
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.Vertical.CenterVertically
                    ) {
                        // 1. Top Row: Golden Badge on Left + Gregorian Date Chip on Right
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            // Hijri Date Badge
                            Row(
                                modifier = GlanceModifier
                                    .background(ImageProvider(R.drawable.bg_hijri_badge))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.ic_calendar_gold),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(14.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(5.dp))
                                Text(
                                    text = "HIJRI DATE",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color(0xFFFCD34D)),
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // Gregorian Date Chip
                            Row(
                                modifier = GlanceModifier
                                    .background(ImageProvider(R.drawable.bg_gregorian_chip))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Text(
                                    text = hijriInfo.gregorianFormatted,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = ColorProvider(Color(0xFFFDE68A)),
                                        fontSize = 10.sp
                                    ),
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = GlanceModifier.defaultWeight())

                        // 2. Middle: Large Day Number + Month & Year
                        Column(modifier = GlanceModifier.fillMaxWidth()) {
                            Text(
                                text = hijriInfo.hijriDay,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = ColorProvider(Color(0xFFFEF3C7)),
                                    fontSize = 40.sp
                                )
                            )
                            Spacer(modifier = GlanceModifier.height(1.dp))
                            Text(
                                text = "${hijriInfo.hijriMonth} ${hijriInfo.hijriYear} AH",
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = ColorProvider(Color(0xFFFBBF24)),
                                    fontSize = 16.sp
                                ),
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = GlanceModifier.defaultWeight())

                        // 3. Subtle Golden Divider Line
                        Box(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(ColorProvider(Color(0xFF382A14)))
                        ) {}

                        Spacer(modifier = GlanceModifier.height(8.dp))

                        // 4. Bottom Row: Star Icon + Event on Left | View Calendar > on Right
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            Row(
                                modifier = GlanceModifier.defaultWeight(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.ic_sparkle_gold),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(14.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(6.dp))
                                Text(
                                    text = hijriInfo.upcomingEvent,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        color = ColorProvider(Color(0xFFF3EDE2)),
                                        fontSize = 13.sp
                                    ),
                                    maxLines = 1
                                )
                            }

                            Text(
                                text = "View Calendar ›",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = ColorProvider(Color(0xFFFBBF24)),
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadHijriDateInfo(context: Context): HijriDateDisplayInfo {
        return try {
            val koin = GlobalContext.getOrNull()
            val prayerTimeDao = koin?.getOrNull<PrayerTimeDao>()

            val currentDateStr = DateUtil.getCurrentDate()
            val entity = prayerTimeDao?.getTodayPrayerTimeOneShot(currentDateStr)

            val now = System.currentTimeMillis()
            val nextHoliday = prayerTimeDao?.getNextUpcomingHoliday(now / 1000)

            val eventText = if (nextHoliday != null && nextHoliday.holidays.isNotEmpty()) {
                val holidayName = nextHoliday.holidays.firstOrNull { it.isNotBlank() } ?: ""
                val hijriDateShort = nextHoliday.hijriDate
                if (holidayName.isNotBlank() && !holidayName.contains("[]")) {
                    holidayName
                } else if (hijriDateShort.isNotBlank()) {
                    hijriDateShort
                } else {
                    "12 Rabi' al-Awwal"
                }
            } else if (entity != null && entity.holidays.isNotEmpty()) {
                val todayHoliday = entity.holidays.firstOrNull { it.isNotBlank() } ?: ""
                if (todayHoliday.isNotBlank() && !todayHoliday.contains("[]")) {
                    todayHoliday
                } else {
                    "12 Rabi' al-Awwal"
                }
            } else {
                "12 Rabi' al-Awwal"
            }

            if (entity != null && entity.hijriDay > 0) {
                val day = entity.hijriDay.toString()
                val month = entity.hijriMonthEn.ifBlank { "Rabi' al-Awwal" }
                val year = entity.hijriYear.toString().ifBlank { "1447" }

                val weekday = entity.gregorianWeekday.ifBlank {
                    SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(now))
                }
                val gDay = entity.gregorianDay.ifBlank {
                    SimpleDateFormat("d", Locale.getDefault()).format(Date(now))
                }
                val gMonth = entity.gregorianMonthName.ifBlank {
                    SimpleDateFormat("MMMM", Locale.getDefault()).format(Date(now))
                }
                val gYear = entity.gregorianYear.ifBlank {
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(now))
                }
                val gregorianFormatted = "$weekday, $gDay $gMonth $gYear"

                HijriDateDisplayInfo(
                    hijriDay = day,
                    hijriMonth = month,
                    hijriYear = year,
                    gregorianFormatted = gregorianFormatted,
                    upcomingEvent = eventText
                )
            } else {
                fallbackInfo(now)
            }
        } catch (e: Exception) {
            fallbackInfo(System.currentTimeMillis())
        }
    }

    private fun fallbackInfo(now: Long): HijriDateDisplayInfo {
        val gregorianFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return HijriDateDisplayInfo(
            hijriDay = "15",
            hijriMonth = "Rabi' al-Awwal",
            hijriYear = "1447",
            gregorianFormatted = gregorianFormat.format(Date(now)),
            upcomingEvent = "12 Rabi' al-Awwal"
        )
    }
}

class OpenCalendarActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        NavigationCommandBus.navigateTo(NavigationTarget.Calendar)
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(MainActivity.EXTRA_NAV_TARGET, MainActivity.NAV_TARGET_CALENDAR)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
    }
}

data class HijriDateDisplayInfo(
    val hijriDay: String,
    val hijriMonth: String,
    val hijriYear: String,
    val gregorianFormatted: String,
    val upcomingEvent: String
)
