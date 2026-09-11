package com.hazrat.islam24.widget.calendar

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
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
import androidx.glance.appwidget.action.actionStartActivity
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
import com.hazrat.islam24.widget.glanceColor
import com.hazrat.calendar.HijriCalendarDay
import com.hazrat.calendar.HijriCalendarHelper
import com.hazrat.calendar.HijriMonthData
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.islam24.main.navigation.NavigationCommandBus
import com.hazrat.islam24.main.navigation.NavigationTarget
import com.hazrat.ui.R
import java.text.SimpleDateFormat
import java.util.Date

class HijriCalendarWidget : GlanceAppWidget() {

    companion object {
        private val TILE_1X1 = DpSize(60.dp, 60.dp)
        private val COMPACT_2X2 = DpSize(120.dp, 120.dp)
        private val SLIM_4X1 = DpSize(220.dp, 60.dp)
        private val TALL_2X3 = DpSize(130.dp, 200.dp)
        private val FULL_4X4 = DpSize(220.dp, 110.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(TILE_1X1, COMPACT_2X2, SLIM_4X1, TALL_2X3, FULL_4X4)
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val monthData = loadCalendarMonthData()
        val weeks = buildWeeksGrid(monthData)
        val todayDay = monthData.days.firstOrNull { it.isToday } ?: monthData.days.firstOrNull()
        val currentWeekIndex = weeks.indexOfFirst { week -> week.any { it?.isToday == true } }.coerceAtLeast(0)
        val currentWeek = weeks.getOrNull(currentWeekIndex) ?: weeks.first()

        provideContent {
            val size = LocalSize.current

            val is1x1 = (size.width < 115.dp && size.height < 115.dp) || (size.width < 160.dp && size.height < 85.dp)
            val isNarrow = size.width < 195.dp
            val isSlim4x1 = !isNarrow && size.height < 85.dp
            val is2x3Vertical = isNarrow && size.height >= 165.dp
            val is2x2Minimal = isNarrow && !is2x3Vertical && !is1x1
            // Any standard or wide size (4x2, 4x3, 4x4) renders the FULL CALENDAR MONTH

            val calendarIntent = Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                putExtra(MainActivity.EXTRA_NAV_TARGET, MainActivity.NAV_TARGET_CALENDAR)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val calendarAction = actionStartActivity(calendarIntent)

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ImageProvider(R.drawable.bg_hijri_calendar_widget))
                    .clickable(calendarAction)
                    .padding(
                        horizontal = if (is1x1) 6.dp else if (isNarrow || isSlim4x1) 10.dp else 12.dp,
                        vertical = if (is1x1) 6.dp else if (isNarrow || isSlim4x1) 8.dp else 10.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    // ═════════════════════════════════════════════════════════════
                    // 1. 1x1 SIZE: Only Current Day & Full Date (NO Calendar Grid)
                    // ═════════════════════════════════════════════════════════════
                    is1x1 -> {
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.Vertical.CenterVertically,
                            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                        ) {
                            Text(
                                text = "${monthData.hijriMonthName.take(6).uppercase()} ${monthData.hijriYear}",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = glanceColor(Color(0xFF38BDF8)),
                                    fontSize = 8.sp
                                ),
                                maxLines = 1
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Text(
                                text = "${todayDay?.hijriDay ?: 12}",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = glanceColor(Color.White),
                                    fontSize = 28.sp
                                )
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Text(
                                text = SimpleDateFormat("EEE, d MMM", LocalLocale.current.platformLocale).format(Date()),
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = glanceColor(Color(0xFF7DD3FC)),
                                    fontSize = 8.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }

                    // ═════════════════════════════════════════════════════════════
                    // 2. 2x2 SIZE: Minimal Calendar with Current Day Big Highlight
                    // ═════════════════════════════════════════════════════════════
                    is2x2Minimal -> {
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            // Header Tag + Icon
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.ic_calendar_cyan),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(12.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                Text(
                                    text = "HIJRI CALENDAR",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = glanceColor(Color(0xFF38BDF8)),
                                        fontSize = 8.sp
                                    )
                                )
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // Big Highlight Box for Today
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Box(
                                    modifier = GlanceModifier
                                        .size(36.dp)
                                        .background(ImageProvider(R.drawable.bg_cal_today_glow)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${todayDay?.hijriDay ?: 12}",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = glanceColor(Color.White),
                                            fontSize = 18.sp
                                        )
                                    )
                                }

                                Spacer(modifier = GlanceModifier.width(8.dp))

                                Column(modifier = GlanceModifier.defaultWeight()) {
                                    Text(
                                        text = monthData.hijriMonthName,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = glanceColor(Color.White),
                                            fontSize = 13.sp
                                        ),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${monthData.hijriYear} AH",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium,
                                            color = glanceColor(Color(0xFF7DD3FC)),
                                            fontSize = 10.sp
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // 7-Day Current Week Minimal Strip
                            WeekdayHeaderRow(fontSize = 7.sp)
                            Spacer(modifier = GlanceModifier.height(1.dp))
                            WeekRow(currentWeek, boxSize = 16.dp, fontSize = 8.sp)
                        }
                    }

                    // ═════════════════════════════════════════════════════════════
                    // 3. 4x1 SLIM BAR: 1-Week Horizontal Strip
                    // ═════════════════════════════════════════════════════════════
                    isSlim4x1 -> {
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            // Left: Month & Year
                            Column(modifier = GlanceModifier.width(80.dp)) {
                                Text(
                                    text = monthData.hijriMonthName,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = glanceColor(Color.White),
                                        fontSize = 12.sp
                                    ),
                                    maxLines = 1
                                )
                                Text(
                                    text = "${monthData.hijriYear} AH",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = glanceColor(Color(0xFF7DD3FC)),
                                        fontSize = 9.sp
                                    ),
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = GlanceModifier.width(4.dp))

                            // Right: 1-Week Strip
                            Column(modifier = GlanceModifier.defaultWeight()) {
                                WeekdayHeaderRow(fontSize = 8.sp)
                                Spacer(modifier = GlanceModifier.height(1.dp))
                                WeekRow(currentWeek, boxSize = 18.dp, fontSize = 9.sp)
                            }
                        }
                    }

                    // ═════════════════════════════════════════════════════════════
                    // 4. 2x3 VERTICAL: Big Today + Strip + Events
                    // ═════════════════════════════════════════════════════════════
                    is2x3Vertical -> {
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            // Section 1: Header + Today Number + Month
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                Row(
                                    modifier = GlanceModifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {
                                    Image(
                                        provider = ImageProvider(R.drawable.ic_calendar_cyan),
                                        contentDescription = null,
                                        modifier = GlanceModifier.size(13.dp)
                                    )
                                    Spacer(modifier = GlanceModifier.width(4.dp))
                                    Text(
                                        text = "HIJRI CALENDAR",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = glanceColor(Color(0xFF38BDF8)),
                                            fontSize = 9.sp
                                        )
                                    )
                                }

                                Spacer(modifier = GlanceModifier.height(4.dp))

                                Text(
                                    text = "${todayDay?.hijriDay ?: 12}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = glanceColor(Color.White),
                                        fontSize = 26.sp
                                    )
                                )
                                Text(
                                    text = "${monthData.hijriMonthName} ${monthData.hijriYear}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = glanceColor(Color(0xFF7DD3FC)),
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Spacer(modifier = GlanceModifier.height(6.dp))

                            // Section 2: Weekday Strip
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                WeekdayHeaderRow(fontSize = 8.sp)
                                Spacer(modifier = GlanceModifier.height(1.dp))
                                WeekRow(currentWeek, boxSize = 16.dp, fontSize = 8.sp)
                            }

                            Spacer(modifier = GlanceModifier.height(6.dp))

                            // Section 3: Upcoming Events
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                Box(
                                    modifier = GlanceModifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color(0x3038BDF8))
                                ) {}

                                Spacer(modifier = GlanceModifier.height(4.dp))

                                Text(
                                    text = "UPCOMING EVENTS",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = glanceColor(Color(0xFF38BDF8)),
                                        fontSize = 8.sp
                                    )
                                )

                                Spacer(modifier = GlanceModifier.height(2.dp))

                                monthData.events.take(2).forEach { event ->
                                    Row(
                                        modifier = GlanceModifier
                                            .fillMaxWidth()
                                            .padding(vertical = 1.dp),
                                        verticalAlignment = Alignment.Vertical.CenterVertically
                                    ) {
                                        Text(
                                            text = "✦ Day ${event.hijriDay}",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = glanceColor(Color(0xFF38BDF8)),
                                                fontSize = 9.sp
                                            )
                                        )
                                        Spacer(modifier = GlanceModifier.defaultWeight())
                                        Text(
                                            text = event.title,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Normal,
                                                color = glanceColor(Color(0xFFCBD5E1)),
                                                fontSize = 8.sp
                                            ),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ═════════════════════════════════════════════════════════════
                    // 5. FULL CALENDAR MONTH (4x2, 4x3, 4x4, 5x4 - ALL 5-6 WEEKS FULLY DISPLAYED!)
                    // ═════════════════════════════════════════════════════════════
                    else -> {
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            // ── Header Row: Icon + Month/Year + Full Tag ──
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Box(
                                    modifier = GlanceModifier
                                        .size(28.dp)
                                        .background(ImageProvider(R.drawable.bg_cal_header_icon)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        provider = ImageProvider(R.drawable.ic_calendar_cyan),
                                        contentDescription = null,
                                        modifier = GlanceModifier.size(14.dp)
                                    )
                                }

                                Spacer(modifier = GlanceModifier.width(8.dp))

                                Column {
                                    Text(
                                        text = "Hijri Calendar",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = glanceColor(Color.White),
                                            fontSize = 14.sp
                                        )
                                    )
                                    Spacer(modifier = GlanceModifier.height(1.dp))
                                    Text(
                                        text = "${monthData.hijriMonthName} ${monthData.hijriYear}",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Normal,
                                            color = glanceColor(Color(0xFF7DD3FC)),
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Spacer(modifier = GlanceModifier.defaultWeight())

                                Text(
                                    text = "Full",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = glanceColor(Color(0xFF38BDF8)),
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // ── Weekday Header Row (S M T W T F S) ──
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                val weekdayHeaders = listOf("S", "M", "T", "W", "T", "F", "S")
                                weekdayHeaders.forEach { header ->
                                    Box(
                                        modifier = GlanceModifier.defaultWeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = header,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = glanceColor(Color(0xFF64748B)),
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = GlanceModifier.height(2.dp))

                            // ── FULL Month Grid: ALL Weeks Rendered ──
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                weeks.forEach { week ->
                                    Row(
                                        modifier = GlanceModifier
                                            .fillMaxWidth()
                                            .padding(vertical = 1.dp),
                                        verticalAlignment = Alignment.Vertical.CenterVertically
                                    ) {
                                        week.forEach { day ->
                                            Box(
                                                modifier = GlanceModifier.defaultWeight(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (day != null) {
                                                    when {
                                                        day.isToday -> {
                                                            Box(
                                                                modifier = GlanceModifier
                                                                    .size(24.dp)
                                                                    .background(ImageProvider(R.drawable.bg_cal_today_glow)),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(
                                                                    text = "${day.hijriDay}",
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        color = glanceColor(Color.White),
                                                                        fontSize = 11.sp
                                                                    )
                                                                )
                                                            }
                                                        }
                                                        day.hasEvent -> {
                                                            Box(
                                                                modifier = GlanceModifier
                                                                    .size(24.dp)
                                                                    .background(ImageProvider(R.drawable.bg_cal_event_chip)),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(
                                                                    text = "${day.hijriDay}",
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Bold,
                                                                        color = glanceColor(Color(0xFF38BDF8)),
                                                                        fontSize = 11.sp
                                                                    )
                                                                )
                                                            }
                                                        }
                                                        else -> {
                                                            Box(
                                                                modifier = GlanceModifier.size(24.dp),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(
                                                                    text = "${day.hijriDay}",
                                                                    style = TextStyle(
                                                                        fontWeight = FontWeight.Normal,
                                                                        color = glanceColor(Color(0xFFCBD5E1)),
                                                                        fontSize = 11.sp
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadCalendarMonthData(): HijriMonthData {
        return try {
            HijriCalendarHelper.getMonthDataForOffset(0)
        } catch (e: Exception) {
            HijriCalendarHelper.loadMonthData(1447, 3)
        }
    }

    private fun buildWeeksGrid(monthData: HijriMonthData): List<List<HijriCalendarDay?>> {
        val weeks = mutableListOf<List<HijriCalendarDay?>>()
        var currentWeek = mutableListOf<HijriCalendarDay?>()

        for (i in 0 until monthData.startPaddingCount) {
            currentWeek.add(null)
        }

        for (day in monthData.days) {
            currentWeek.add(day)
            if (currentWeek.size == 7) {
                weeks.add(currentWeek)
                currentWeek = mutableListOf()
            }
        }

        if (currentWeek.isNotEmpty()) {
            while (currentWeek.size < 7) {
                currentWeek.add(null)
            }
            weeks.add(currentWeek)
        }

        return weeks
    }
}

// ── Glance Composable Helpers ──

@androidx.compose.runtime.Composable
private fun WeekdayHeaderRow(fontSize: androidx.compose.ui.unit.TextUnit = 10.sp) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        val weekdayHeaders = listOf("S", "M", "T", "W", "T", "F", "S")
        weekdayHeaders.forEach { header ->
            Box(
                modifier = GlanceModifier.defaultWeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = header,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = glanceColor(Color(0xFF64748B)),
                        fontSize = fontSize
                    )
                )
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun WeekRow(
    week: List<HijriCalendarDay?>,
    boxSize: androidx.compose.ui.unit.Dp = 22.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 10.sp
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        week.forEach { day ->
            Box(
                modifier = GlanceModifier.defaultWeight(),
                contentAlignment = Alignment.Center
            ) {
                if (day != null) {
                    when {
                        day.isToday -> {
                            Box(
                                modifier = GlanceModifier
                                    .size(boxSize)
                                    .background(ImageProvider(R.drawable.bg_cal_today_glow)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${day.hijriDay}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = glanceColor(Color.White),
                                        fontSize = fontSize
                                    )
                                )
                            }
                        }
                        day.hasEvent -> {
                            Box(
                                modifier = GlanceModifier
                                    .size(boxSize)
                                    .background(ImageProvider(R.drawable.bg_cal_event_chip)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${day.hijriDay}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = glanceColor(Color(0xFF38BDF8)),
                                        fontSize = fontSize
                                    )
                                )
                            }
                        }
                        else -> {
                            Box(
                                modifier = GlanceModifier.size(boxSize),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${day.hijriDay}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        color = glanceColor(Color(0xFFCBD5E1)),
                                        fontSize = fontSize
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

class OpenCalendarScreenActionCallback : ActionCallback {
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
