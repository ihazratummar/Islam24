package com.hazrat.islam24.widget.nextprayer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import android.widget.Toast
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
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
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
import com.hazrat.database.dao.prayer.PrayerLogDao
import com.hazrat.database.dao.prayer.PrayerTimeDao
import com.hazrat.database.entity.prayer.PrayerLogEntity
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.islam24.main.navigation.NavigationCommandBus
import com.hazrat.islam24.main.navigation.NavigationTarget
import com.hazrat.model.Prayer
import com.hazrat.ui.R
import com.hazrat.utils.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import org.koin.core.context.GlobalContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

val prayerNameActionKey = ActionParameters.Key<String>("active_prayer_name")

class NextPrayerWidget : GlanceAppWidget() {

    companion object {
        private val SLIM_BAR = DpSize(200.dp, 60.dp)
        private val SMALL_SQUARE = DpSize(120.dp, 120.dp)
        private val TALL_COLUMN = DpSize(140.dp, 200.dp)
        private val MEDIUM_WIDE = DpSize(240.dp, 110.dp)
        private val LARGE_EXPANDED = DpSize(280.dp, 220.dp)
        private const val REFRESH_REQUEST_CODE = 9001

        fun scheduleMinuteRefresh(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NextPrayerWidgetReceiver::class.java).apply {
                action = NextPrayerWidgetReceiver.ACTION_REFRESH_WIDGET
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, REFRESH_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 600_000L,
                600_000L,
                pendingIntent
            )
        }

        fun cancelMinuteRefresh(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NextPrayerWidgetReceiver::class.java).apply {
                action = NextPrayerWidgetReceiver.ACTION_REFRESH_WIDGET
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, REFRESH_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(SLIM_BAR, SMALL_SQUARE, TALL_COLUMN, MEDIUM_WIDE, LARGE_EXPANDED)
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val prayerInfo = loadPrayerInfo(context)

        // Live ticking Chronometer
        val chronometerViews = RemoteViews(context.packageName, R.layout.widget_chronometer_countdown)
        val remainingMillis = prayerInfo.remainingMillis
        chronometerViews.setChronometerCountDown(R.id.widget_chronometer, true)
        chronometerViews.setChronometer(
            R.id.widget_chronometer,
            SystemClock.elapsedRealtime() + remainingMillis,
            null,
            true
        )

        provideContent {
            val size = LocalSize.current

            // ── Clean Robust Sizing Boundaries ──
            val isNarrow = size.width < 210.dp
            val isShort = size.height < 85.dp
            val isLarge = !isNarrow && size.height >= 170.dp
            val isTallNarrow = isNarrow && size.height >= 165.dp
            val isCompactNarrow = isNarrow && !isTallNarrow
            val isSlimWide = !isNarrow && isShort
            val isStandardWide = !isNarrow && !isShort && !isLarge

            val prayerTimeIntent = Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                putExtra(MainActivity.EXTRA_NAV_TARGET, MainActivity.NAV_TARGET_PRAYER_TIME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val openPrayerAction = actionStartActivity(prayerTimeIntent)

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ImageProvider(R.drawable.bg_next_prayer_widget))
                    .clickable(openPrayerAction)
                    .padding(
                        horizontal = if (isNarrow || isShort) 12.dp else 16.dp,
                        vertical = if (isNarrow || isShort) 10.dp else 14.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    // ═════════════════════════════════════════════════════════════
                    // 1. WIDE & SLIM BAR (3x1, 4x1: width >= 210dp, height < 85dp)
                    // ═════════════════════════════════════════════════════════════
                    isSlimWide -> {
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            Image(
                                provider = ImageProvider(R.drawable.dot_mint_glow),
                                contentDescription = null,
                                modifier = GlanceModifier.size(7.dp)
                            )
                            Spacer(modifier = GlanceModifier.width(5.dp))
                            Text(
                                text = prayerInfo.prayerName,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = ColorProvider(Color.White),
                                    fontSize = 17.sp
                                ),
                                maxLines = 1
                            )
                            Spacer(modifier = GlanceModifier.width(5.dp))
                            Text(
                                text = prayerInfo.rawTimeStr,
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = ColorProvider(Color(0xFF99E6DB)),
                                    fontSize = 12.sp
                                ),
                                maxLines = 1
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())

                            if (prayerInfo.isNow) {
                                Text(
                                    text = "NOW",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color(0xFF5EEAD4)),
                                        fontSize = 16.sp
                                    )
                                )
                            } else {
                                Text(
                                    text = "IN",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color(0xFF80CBC4)),
                                        fontSize = 9.sp
                                    )
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                AndroidRemoteViews(
                                    remoteViews = chronometerViews,
                                    modifier = GlanceModifier.width(75.dp).height(22.dp)
                                )
                            }

                            Spacer(modifier = GlanceModifier.width(5.dp))
                            Image(
                                provider = ImageProvider(prayerInfo.iconRes),
                                contentDescription = null,
                                modifier = GlanceModifier.size(14.dp)
                            )
                        }
                    }

                    // ═════════════════════════════════════════════════════════════
                    // 2. NARROW & COMPACT (2x1, 2x2: width < 210dp, height < 165dp)
                    // (Zero horizontal squeeze - Pure clean vertical stack!)
                    // ═════════════════════════════════════════════════════════════
                    isCompactNarrow -> {
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            // Top Row: Header + Icon
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.dot_mint_glow),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(6.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                Text(
                                    text = prayerInfo.headerLabel,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color(0xFF5EEAD4)),
                                        fontSize = 9.sp
                                    )
                                )
                                Spacer(modifier = GlanceModifier.defaultWeight())
                                Image(
                                    provider = ImageProvider(prayerInfo.iconRes),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(13.dp)
                                )
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // Center: Prayer Name & Time Stacked
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                Text(
                                    text = prayerInfo.prayerName,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color.White),
                                        fontSize = 24.sp
                                    ),
                                    maxLines = 1
                                )
                                Spacer(modifier = GlanceModifier.height(1.dp))
                                Text(
                                    text = prayerInfo.prayerTimeFormatted,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = ColorProvider(Color(0xFF99E6DB)),
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // Bottom: If isNow -> Log Prayer button, else -> IN + Countdown
                            if (prayerInfo.isNow) {
                                ButtonRow(prayerInfo, isCompact = true)
                            } else {
                                Row(
                                    modifier = GlanceModifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {
                                    Text(
                                        text = "IN",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = ColorProvider(Color(0xFF80CBC4)),
                                            fontSize = 9.sp
                                        )
                                    )
                                    Spacer(modifier = GlanceModifier.width(4.dp))
                                    AndroidRemoteViews(
                                        remoteViews = chronometerViews,
                                        modifier = GlanceModifier.width(80.dp).height(22.dp)
                                    )
                                }
                            }
                        }
                    }

                    // ═════════════════════════════════════════════════════════════
                    // 3. NARROW & TALL (2x3, 2x4: width < 210dp, height >= 165dp)
                    // ═════════════════════════════════════════════════════════════
                    isTallNarrow -> {
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.Vertical.Top
                        ) {
                            // Top Header
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.dot_mint_glow),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(7.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                Text(
                                    text = prayerInfo.headerLabel,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color(0xFF5EEAD4)),
                                        fontSize = 9.sp
                                    )
                                )
                                Spacer(modifier = GlanceModifier.defaultWeight())
                                Image(
                                    provider = ImageProvider(prayerInfo.iconRes),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(14.dp)
                                )
                            }

                            Spacer(modifier = GlanceModifier.height(4.dp))

                            // Hero Prayer Name & Time
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                Text(
                                    text = prayerInfo.prayerName,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color.White),
                                        fontSize = 24.sp
                                    ),
                                    maxLines = 1
                                )
                                Text(
                                    text = prayerInfo.prayerTimeFormatted,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = ColorProvider(Color(0xFF99E6DB)),
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = GlanceModifier.height(4.dp))

                            if (prayerInfo.isNow) {
                                ButtonRow(prayerInfo, isCompact = true)
                            } else {
                                Row(
                                    modifier = GlanceModifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {
                                    Text(
                                        text = "IN",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = ColorProvider(Color(0xFF80CBC4)),
                                            fontSize = 9.sp
                                        )
                                    )
                                    Spacer(modifier = GlanceModifier.width(4.dp))
                                    AndroidRemoteViews(
                                        remoteViews = chronometerViews,
                                        modifier = GlanceModifier.width(75.dp).height(22.dp)
                                    )
                                }
                            }

                            Spacer(modifier = GlanceModifier.height(6.dp))

                            // Divider
                            Box(
                                modifier = GlanceModifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(ColorProvider(Color(0x305EEAD4)))
                            ) {}

                            Spacer(modifier = GlanceModifier.height(4.dp))

                            // Daily Prayer Schedule List (5 Daily Prayers)
                            val fivePrayers = prayerInfo.allDailyPrayers.filter { it.name != "Sunrise" }
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                fivePrayers.forEach { item ->
                                    Row(
                                        modifier = GlanceModifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Vertical.CenterVertically
                                    ) {
                                        Text(
                                            text = if (item.isLogged) "✓ ${item.name}" else item.name,
                                            style = TextStyle(
                                                fontWeight = if (item.isCurrentOrNext) FontWeight.Bold else FontWeight.Normal,
                                                color = ColorProvider(if (item.isCurrentOrNext) Color(0xFF5EEAD4) else Color.White),
                                                fontSize = 10.sp
                                            ),
                                            maxLines = 1
                                        )
                                        Spacer(modifier = GlanceModifier.defaultWeight())
                                        Text(
                                            text = item.shortTime,
                                            style = TextStyle(
                                                fontWeight = if (item.isCurrentOrNext) FontWeight.Bold else FontWeight.Normal,
                                                color = ColorProvider(if (item.isCurrentOrNext) Color(0xFF5EEAD4) else Color(0xFF99E6DB)),
                                                fontSize = 10.sp
                                            ),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ═════════════════════════════════════════════════════════════
                    // 4. WIDE & LARGE EXPANDED (4x3, 4x4: width >= 210dp, height >= 170dp)
                    // (Full-height balanced breathing space with Spacers!)
                    // ═════════════════════════════════════════════════════════════
                    isLarge -> {
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            // Top Section: Header + Hero Card + Action
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                // 1. Top Header Row: Live Tag + Date Info + Icon
                                Row(
                                    modifier = GlanceModifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {
                                    Image(
                                        provider = ImageProvider(R.drawable.dot_mint_glow),
                                        contentDescription = null,
                                        modifier = GlanceModifier.size(8.dp)
                                    )
                                    Spacer(modifier = GlanceModifier.width(6.dp))
                                    Text(
                                        text = prayerInfo.headerLabel,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = ColorProvider(Color(0xFF5EEAD4)),
                                            fontSize = 11.sp
                                        )
                                    )
                                    Spacer(modifier = GlanceModifier.defaultWeight())
                                    Text(
                                        text = "${prayerInfo.gregorianDateFormatted} • ${prayerInfo.hijriDateFormatted}",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium,
                                            color = ColorProvider(Color(0xFF99E6DB)),
                                            fontSize = 10.sp
                                        )
                                    )
                                    Spacer(modifier = GlanceModifier.width(6.dp))
                                    Image(
                                        provider = ImageProvider(prayerInfo.iconRes),
                                        contentDescription = null,
                                        modifier = GlanceModifier.size(16.dp)
                                    )
                                }

                                Spacer(modifier = GlanceModifier.height(8.dp))

                                // 2. Hero Current/Next Prayer Card
                                Row(
                                    modifier = GlanceModifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {
                                    Column(modifier = GlanceModifier.defaultWeight()) {
                                        Text(
                                            text = prayerInfo.prayerName,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = ColorProvider(Color.White),
                                                fontSize = 32.sp
                                            ),
                                            maxLines = 1
                                        )
                                        Spacer(modifier = GlanceModifier.height(1.dp))
                                        Text(
                                            text = prayerInfo.prayerTimeFormatted,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Medium,
                                                color = ColorProvider(Color(0xFF99E6DB)),
                                                fontSize = 13.sp
                                            ),
                                            maxLines = 1
                                        )
                                    }

                                    if (prayerInfo.isNow) {
                                        Text(
                                            text = "NOW",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = ColorProvider(Color(0xFF5EEAD4)),
                                                fontSize = 32.sp
                                            )
                                        )
                                    } else {
                                        Column(
                                            horizontalAlignment = Alignment.Horizontal.End,
                                            verticalAlignment = Alignment.Vertical.Bottom
                                        ) {
                                            Text(
                                                text = "IN",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    color = ColorProvider(Color(0xFF80CBC4)),
                                                    fontSize = 10.sp
                                                )
                                            )
                                            Spacer(modifier = GlanceModifier.height(1.dp))
                                            AndroidRemoteViews(
                                                remoteViews = chronometerViews,
                                                modifier = GlanceModifier.width(105.dp).height(26.dp)
                                            )
                                        }
                                    }
                                }

                                if (prayerInfo.isNow) {
                                    Spacer(modifier = GlanceModifier.height(6.dp))
                                    ButtonRow(prayerInfo, isCompact = false)
                                }
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // 3. Subtle Divider
                            Box(
                                modifier = GlanceModifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(ColorProvider(Color(0x305EEAD4)))
                            ) {}

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // Bottom Section: Full 6-Prayer Schedule Grid + Progress Footer
                            Column(modifier = GlanceModifier.fillMaxWidth()) {
                                Row(modifier = GlanceModifier.fillMaxWidth()) {
                                    val firstThree = prayerInfo.allDailyPrayers.take(3)
                                    firstThree.forEach { item ->
                                        PrayerGridItem(item, modifier = GlanceModifier.defaultWeight())
                                    }
                                }

                                Spacer(modifier = GlanceModifier.height(6.dp))

                                Row(modifier = GlanceModifier.fillMaxWidth()) {
                                    val lastThree = prayerInfo.allDailyPrayers.drop(3)
                                    lastThree.forEach { item ->
                                        PrayerGridItem(item, modifier = GlanceModifier.defaultWeight())
                                    }
                                }

                                Spacer(modifier = GlanceModifier.height(8.dp))

                                // 5. Progress Footer
                                Row(
                                    modifier = GlanceModifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {
                                    Text(
                                        text = "Prayers Logged: ${prayerInfo.completedCount} of 5",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium,
                                            color = ColorProvider(Color(0xFF5EEAD4)),
                                            fontSize = 10.sp
                                        )
                                    )
                                    Spacer(modifier = GlanceModifier.defaultWeight())
                                    Text(
                                        text = "View Schedule ›",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = ColorProvider(Color.White),
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // ═════════════════════════════════════════════════════════════
                    // 5. WIDE & STANDARD MEDIUM (4x2: width >= 210dp, 85dp <= height < 170dp)
                    // ═════════════════════════════════════════════════════════════
                    else -> {
                        Column(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            // Top Row: Header + Icon
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.dot_mint_glow),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(7.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(5.dp))
                                Text(
                                    text = prayerInfo.headerLabel,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color(0xFF5EEAD4)),
                                        fontSize = 10.sp
                                    )
                                )
                                Spacer(modifier = GlanceModifier.defaultWeight())
                                Image(
                                    provider = ImageProvider(prayerInfo.iconRes),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // Middle Row: Prayer Name + Time (Left) | NOW (Big) or IN + Live Countdown (Right)
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {
                                Column(modifier = GlanceModifier.defaultWeight()) {
                                    Text(
                                        text = prayerInfo.prayerName,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = ColorProvider(Color.White),
                                            fontSize = 28.sp
                                        ),
                                        maxLines = 1
                                    )
                                    Spacer(modifier = GlanceModifier.height(1.dp))
                                    Text(
                                        text = prayerInfo.prayerTimeFormatted,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium,
                                            color = ColorProvider(Color(0xFF99E6DB)),
                                            fontSize = 12.sp
                                        ),
                                        maxLines = 1
                                    )
                                }

                                if (prayerInfo.isNow) {
                                    Text(
                                        text = "NOW",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = ColorProvider(Color(0xFF5EEAD4)),
                                            fontSize = 30.sp
                                        )
                                    )
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.Horizontal.End,
                                        verticalAlignment = Alignment.Vertical.Bottom
                                    ) {
                                        Text(
                                            text = "IN",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = ColorProvider(Color(0xFF80CBC4)),
                                                fontSize = 9.sp
                                            )
                                        )
                                        Spacer(modifier = GlanceModifier.height(1.dp))
                                        AndroidRemoteViews(
                                            remoteViews = chronometerViews,
                                            modifier = GlanceModifier.width(105.dp).height(26.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = GlanceModifier.defaultWeight())

                            // Bottom Section: If isNow -> Button, else -> 5-Prayer Timeline Strip
                            if (prayerInfo.isNow) {
                                ButtonRow(prayerInfo, isCompact = false)
                            } else {
                                Row(
                                    modifier = GlanceModifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {
                                    val timelinePrayers = prayerInfo.allDailyPrayers.filter { it.name != "Sunrise" }
                                    timelinePrayers.forEach { item ->
                                        Box(
                                            modifier = GlanceModifier
                                                .defaultWeight()
                                                .height(22.dp)
                                                .background(
                                                    ImageProvider(
                                                        if (item.isCurrentOrNext) R.drawable.bg_prayer_timeline_active
                                                        else R.drawable.bg_prayer_timeline_inactive
                                                    )
                                                )
                                                .padding(horizontal = 2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (item.isLogged) "✓ ${item.name}" else "${item.name} ${item.shortTime}",
                                                style = TextStyle(
                                                    fontWeight = if (item.isCurrentOrNext) FontWeight.Bold else FontWeight.Normal,
                                                    color = ColorProvider(if (item.isCurrentOrNext) Color.White else Color(0xFF99E6DB)),
                                                    fontSize = 8.sp
                                                ),
                                                maxLines = 1
                                            )
                                        }
                                        Spacer(modifier = GlanceModifier.width(2.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadPrayerInfo(context: Context): NextPrayerDisplayInfo {
        return try {
            val koin = GlobalContext.getOrNull()
            val fallbackDb by lazy {
                try {
                    androidx.room.Room.databaseBuilder(
                        context.applicationContext,
                        com.hazrat.database.database.PrayerDatabase::class.java,
                        "prayer-database"
                    ).fallbackToDestructiveMigration(dropAllTables = false).build()
                } catch (_: Exception) { null }
            }
            val prayerLogDao = koin?.getOrNull<PrayerLogDao>() ?: fallbackDb?.prayerLogDao()
            val prayerTimeDao = koin?.getOrNull<PrayerTimeDao>() ?: fallbackDb?.prayerTimeDao()

            val now = System.currentTimeMillis()
            val jDate = LocalDate.now()
            val kDate = kotlinx.datetime.LocalDate(jDate.year, jDate.monthValue, jDate.dayOfMonth)
            val logEntity = prayerLogDao?.getLogByDate(kDate)

            val currentDateStr = DateUtil.getCurrentDate()
            val entity = prayerTimeDao?.getTodayPrayerTimeOneShot(currentDateStr)

            if (entity != null && entity.fajrTime > 0L) {
                val fajr = entity.fajrTime
                val sunrise = entity.sunriseTime
                val dhuhr = entity.dhuhrTime
                val asr = entity.asrTime
                val maghrib = entity.maghribTime
                val isha = entity.ishaTime
                val midnight = entity.midnightTime

                val prayers = listOf(
                    Triple("Fajr", fajr, R.drawable.ic_sun_mint),
                    Triple("Sunrise", sunrise, R.drawable.ic_sun_mint),
                    Triple("Dhuhr", dhuhr, R.drawable.ic_sun_mint),
                    Triple("Asr", asr, R.drawable.ic_sun_mint),
                    Triple("Maghrib", maghrib, R.drawable.ic_sun_mint),
                    Triple("Isha", isha, R.drawable.ic_sun_mint)
                )

                // ── Current active prayer (matches PrayerTimeCalculations) ──
                val currentPrayerName = when {
                    fajr > 0 && now in fajr until sunrise -> "Fajr"
                    sunrise > 0 && now in sunrise until dhuhr -> "Sunrise"
                    dhuhr > 0 && now in dhuhr until asr -> "Dhuhr"
                    asr > 0 && now in asr until maghrib -> "Asr"
                    maghrib > 0 && now in maghrib until isha -> "Maghrib"
                    (isha > 0 && now >= isha) || (fajr > 0 && now < fajr) -> "Isha"
                    else -> null
                }

                // ── isNow: True ONLY during active window of current prayer ──
                val isNow = when (currentPrayerName) {
                    "Fajr" -> now in fajr..(sunrise - TimeUnit.MINUTES.toMillis(10))
                    "Sunrise" -> false
                    "Dhuhr" -> now in dhuhr..(asr - TimeUnit.MINUTES.toMillis(60))
                    "Asr" -> now in asr..(maghrib - TimeUnit.MINUTES.toMillis(30))
                    "Maghrib" -> now in maghrib..(isha - TimeUnit.MINUTES.toMillis(30))
                    "Isha" -> midnight > 0 && now in isha until midnight
                    else -> false
                }

                // ── Next upcoming prayer ──
                val nextPrayerTriple = prayers.firstOrNull { it.second > now }
                    ?: Triple("Fajr", fajr + TimeUnit.DAYS.toMillis(1), R.drawable.ic_sun_mint)
                val nextPrayerName = nextPrayerTriple.first
                val nextPrayerTime = nextPrayerTriple.second

                // ── Display logic: Primary focus is Current Prayer if Now, otherwise Next Prayer ──
                val displayPrayerName: String
                val displayPrayerTimeFormatted: String
                val rawTimeStr: String
                val headerLabel: String
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val shortTimeFormat = SimpleDateFormat("h:mm", Locale.getDefault())

                if (isNow && currentPrayerName != null) {
                    val currentPrayerTime = prayers.firstOrNull { it.first == currentPrayerName }?.second ?: dhuhr
                    displayPrayerName = currentPrayerName
                    rawTimeStr = timeFormat.format(Date(currentPrayerTime))
                    displayPrayerTimeFormatted = "Started at $rawTimeStr"
                    headerLabel = "CURRENT PRAYER"
                } else {
                    displayPrayerName = nextPrayerName
                    rawTimeStr = timeFormat.format(Date(nextPrayerTime))
                    displayPrayerTimeFormatted = rawTimeStr
                    headerLabel = "NEXT PRAYER"
                }

                // Remaining millis until next upcoming prayer for the Chronometer
                val remainingMillis = (nextPrayerTime - now).coerceAtLeast(0L)

                // ── Log button logic ──
                val prayerToLog = if (isNow && currentPrayerName != null) currentPrayerName else nextPrayerName
                val isLogged = when (prayerToLog.lowercase(Locale.ROOT)) {
                    "fajr" -> logEntity?.fajr == true
                    "dhuhr" -> logEntity?.dhuhr == true
                    "asr" -> logEntity?.asr == true
                    "maghrib" -> logEntity?.maghrib == true
                    "isha" -> logEntity?.isha == true
                    else -> false
                }
                val buttonLabel = if (isLogged) "Prayer Logged ✓" else "Log Prayer"

                // ── Full daily schedule items ──
                val targetPrayerForHighlight = if (isNow && currentPrayerName != null) currentPrayerName else nextPrayerName
                val allDailyPrayers = prayers.map { (name, time, icon) ->
                    val isItemLogged = when (name.lowercase(Locale.ROOT)) {
                        "fajr" -> logEntity?.fajr == true
                        "dhuhr" -> logEntity?.dhuhr == true
                        "asr" -> logEntity?.asr == true
                        "maghrib" -> logEntity?.maghrib == true
                        "isha" -> logEntity?.isha == true
                        else -> false
                    }
                    PrayerScheduleItem(
                        name = name,
                        timeFormatted = timeFormat.format(Date(time)),
                        shortTime = shortTimeFormat.format(Date(time)),
                        isLogged = isItemLogged,
                        isCurrentOrNext = name == targetPrayerForHighlight,
                        iconRes = icon
                    )
                }

                val completedCount = listOfNotNull(
                    logEntity?.fajr,
                    logEntity?.dhuhr,
                    logEntity?.asr,
                    logEntity?.maghrib,
                    logEntity?.isha
                ).count { it }

                val gWeekday = entity.gregorianWeekday.ifBlank { "Tue" }
                val gDay = entity.gregorianDay.ifBlank { "25" }
                val gMonth = entity.gregorianMonthName.ifBlank { "Aug" }
                val gregorianDateFormatted = "$gWeekday, $gDay $gMonth"
                val hijriDateFormatted = "${entity.hijriDay} ${entity.hijriMonthEn.ifBlank { "Rabi' I" }}"

                NextPrayerDisplayInfo(
                    headerLabel = headerLabel,
                    prayerName = displayPrayerName,
                    prayerTimeFormatted = displayPrayerTimeFormatted,
                    rawTimeStr = rawTimeStr,
                    remainingMillis = remainingMillis,
                    iconRes = R.drawable.ic_sun_mint,
                    isNow = isNow,
                    buttonLabel = buttonLabel,
                    activeOrPreviousPrayerToLog = prayerToLog,
                    isCurrentPrayerLogged = isLogged,
                    allDailyPrayers = allDailyPrayers,
                    completedCount = completedCount,
                    gregorianDateFormatted = gregorianDateFormatted,
                    hijriDateFormatted = hijriDateFormatted
                )
            } else {
                fallbackInfo()
            }
        } catch (e: Exception) {
            fallbackInfo()
        }
    }

    private fun fallbackInfo(): NextPrayerDisplayInfo {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val nowStr = timeFormat.format(Date())
        return NextPrayerDisplayInfo(
            headerLabel = "NEXT PRAYER",
            prayerName = "Dhuhr",
            prayerTimeFormatted = nowStr,
            rawTimeStr = nowStr,
            remainingMillis = 0L,
            iconRes = R.drawable.ic_sun_mint,
            isNow = false,
            buttonLabel = "Log Prayer",
            activeOrPreviousPrayerToLog = "Dhuhr",
            isCurrentPrayerLogged = false,
            allDailyPrayers = emptyList(),
            completedCount = 0,
            gregorianDateFormatted = "Today",
            hijriDateFormatted = ""
        )
    }
}

// ── Composable Helpers ──

@androidx.compose.runtime.Composable
private fun ButtonRow(prayerInfo: NextPrayerDisplayInfo, isCompact: Boolean) {
    val buttonAction = if (!prayerInfo.isCurrentPrayerLogged) {
        actionRunCallback<LogPrayerActionCallback>(
            actionParametersOf(prayerNameActionKey to prayerInfo.activeOrPreviousPrayerToLog)
        )
    } else {
        actionRunCallback<AlreadyLoggedToastActionCallback>()
    }

    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(if (isCompact) 28.dp else 36.dp)
            .background(ImageProvider(R.drawable.btn_log_prayer_widget))
            .clickable(buttonAction),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Image(
            provider = ImageProvider(R.drawable.ic_prayer_log),
            contentDescription = null,
            modifier = GlanceModifier.size(if (isCompact) 11.dp else 14.dp)
        )
        Spacer(modifier = GlanceModifier.width(5.dp))
        Text(
            text = prayerInfo.buttonLabel,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = ColorProvider(Color.White),
                fontSize = if (isCompact) 11.sp else 13.sp
            ),
            maxLines = 1
        )
    }
}

@androidx.compose.runtime.Composable
private fun PrayerGridItem(item: PrayerScheduleItem, modifier: GlanceModifier = GlanceModifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .background(
                ImageProvider(
                    if (item.isCurrentOrNext) R.drawable.bg_prayer_timeline_active
                    else R.drawable.bg_prayer_timeline_inactive
                )
            )
            .padding(horizontal = 4.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = if (item.isLogged) "✓ ${item.name}" else item.name,
                style = TextStyle(
                    fontWeight = if (item.isCurrentOrNext) FontWeight.Bold else FontWeight.Medium,
                    color = ColorProvider(if (item.isCurrentOrNext) Color.White else Color(0xFF99E6DB)),
                    fontSize = 10.sp
                ),
                maxLines = 1
            )
            Spacer(modifier = GlanceModifier.height(1.dp))
            Text(
                text = item.shortTime,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(if (item.isCurrentOrNext) Color(0xFF5EEAD4) else Color(0xFF70B8AC)),
                    fontSize = 9.sp
                ),
                maxLines = 1
            )
        }
    }
}

// ── Action Callbacks ──

class OpenPrayerScreenActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        NavigationCommandBus.navigateTo(NavigationTarget.PrayerTime)
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(MainActivity.EXTRA_NAV_TARGET, MainActivity.NAV_TARGET_PRAYER_TIME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
    }
}

class LogPrayerActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val prayerName = parameters[prayerNameActionKey] ?: return

        withContext(Dispatchers.IO) {
            val koin = GlobalContext.getOrNull()
            val prayerLogDao = koin?.getOrNull<PrayerLogDao>() ?: return@withContext

            val jDate = LocalDate.now()
            val kDate = kotlinx.datetime.LocalDate(jDate.year, jDate.monthValue, jDate.dayOfMonth)
            val existingLog = prayerLogDao.getLogByDate(kDate)
            val nowInstant = Instant.fromEpochMilliseconds(System.currentTimeMillis())

            val updatedEntity = existingLog?.let {
                when (prayerName.lowercase(Locale.ROOT)) {
                    "fajr" -> it.copy(fajr = true, isSynced = false, updatedAt = nowInstant)
                    "dhuhr" -> it.copy(dhuhr = true, isSynced = false, updatedAt = nowInstant)
                    "asr" -> it.copy(asr = true, isSynced = false, updatedAt = nowInstant)
                    "maghrib" -> it.copy(maghrib = true, isSynced = false, updatedAt = nowInstant)
                    "isha" -> it.copy(isha = true, isSynced = false, updatedAt = nowInstant)
                    else -> it
                }
            } ?: PrayerLogEntity(
                logDate = kDate,
                fajr = prayerName.equals("fajr", ignoreCase = true),
                dhuhr = prayerName.equals("dhuhr", ignoreCase = true),
                asr = prayerName.equals("asr", ignoreCase = true),
                maghrib = prayerName.equals("maghrib", ignoreCase = true),
                isha = prayerName.equals("isha", ignoreCase = true),
                isSynced = false,
                updatedAt = nowInstant
            )

            prayerLogDao.upsert(updatedEntity)
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(context, "$prayerName logged successfully!", Toast.LENGTH_SHORT).show()
        }

        NextPrayerWidget().updateAll(context)
    }
}

class AlreadyLoggedToastActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Prayer already logged for today!", Toast.LENGTH_SHORT).show()
        }
    }
}

// ── Data Classes ──

data class PrayerScheduleItem(
    val name: String,
    val timeFormatted: String,
    val shortTime: String,
    val isLogged: Boolean,
    val isCurrentOrNext: Boolean,
    val iconRes: Int
)

data class NextPrayerDisplayInfo(
    val headerLabel: String,
    val prayerName: String,
    val prayerTimeFormatted: String,
    val rawTimeStr: String,
    val remainingMillis: Long,
    val iconRes: Int,
    val isNow: Boolean,
    val buttonLabel: String,
    val activeOrPreviousPrayerToLog: String,
    val isCurrentPrayerLogged: Boolean,
    val allDailyPrayers: List<PrayerScheduleItem>,
    val completedCount: Int,
    val gregorianDateFormatted: String,
    val hijriDateFormatted: String
)
