package com.hazrat.home.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.common.IslamicGridBackground
import com.hazrat.ui.common.PulsingLiveDot
import com.hazrat.ui.theme.dimens

@Composable
fun HomeScreenWidgetsScreen(
    onBackClick: () -> Unit,
    onPinWidget: (widgetId: String) -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BasicTopBar(
                topBarTitle = stringResource(id = R.string.home_screen_widgets_title),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        IslamicGridBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimens.space16),
                verticalArrangement = Arrangement.spacedBy(dimens.space20)
            ) {
                item {
                    Spacer(modifier = Modifier.height(dimens.space4))
                }

                // 1. Top Intro Banner
                item {
                    WidgetsIntroBanner()
                }

                // 2. Widget 1: Next Prayer Widget Preview Card
                item {
                    WidgetPreviewContainer(
                        iconRes = R.drawable.ic_sun_mint,
                        iconTint = Color(0xFF10B981),
                        title = stringResource(R.string.widget_next_prayer_title),
                        subtitle = stringResource(R.string.widget_next_prayer_desc),
                        onAddClick = { onPinWidget("next_prayer") }
                    ) {
                        NextPrayerWidgetPreview()
                    }
                }

                // 3. Widget 2: Hijri Date & Event Preview Card
                item {
                    WidgetPreviewContainer(
                        iconRes = R.drawable.ic_calendar_gold,
                        iconTint = Color(0xFFFBBF24),
                        title = stringResource(R.string.widget_hijri_date_title),
                        subtitle = stringResource(R.string.widget_hijri_date_desc),
                        onAddClick = { onPinWidget("hijri_date") }
                    ) {
                        HijriDateWidgetPreview()
                    }
                }

                // 4. Widget 3: Hijri Full Calendar Preview Card
                item {
                    WidgetPreviewContainer(
                        iconRes = R.drawable.ic_calendar_cyan,
                        iconTint = Color(0xFF38BDF8),
                        title = stringResource(R.string.widget_hijri_calendar_title),
                        subtitle = stringResource(R.string.widget_hijri_calendar_desc),
                        onAddClick = { onPinWidget("hijri_calendar") }
                    ) {
                        HijriFullCalendarWidgetPreview()
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(dimens.space24))
                }
            }
        }
    }
}

@Composable
private fun WidgetsIntroBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerXl)),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0F5143),
                            Color(0xFF0D6B57),
                            Color(0xFF108B71)
                        )
                    ),
                    shape = RoundedCornerShape(dimens.cornerXl)
                )
                .border(
                    width = dimens.divider,
                    color = Color(0x4034D399),
                    shape = RoundedCornerShape(dimens.cornerXl)
                )
                .padding(dimens.space16)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.space48)
                        .background(
                            color = Color(0x26FFFFFF),
                            shape = RoundedCornerShape(dimens.cornerLg)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_widget_grid),
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconMd),
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Text(
                        text = stringResource(R.string.home_screen_widgets_intro_title),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = stringResource(R.string.home_screen_widgets_intro_desc),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xCCFFFFFF)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun WidgetPreviewContainer(
    iconRes: Int,
    iconTint: Color,
    title: String,
    subtitle: String,
    onAddClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerXl))
            .border(
                width = dimens.divider,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                shape = RoundedCornerShape(dimens.cornerXl)
            ),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.space40)
                        .background(
                            color = iconTint.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(dimens.cornerMd)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = com.hazrat.ui.theme.customColors.secondaryText
                    )
                }

                Button(
                    onClick = onAddClick,
                    shape = RoundedCornerShape(dimens.cornerFull),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D9488)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = dimens.space12,
                        vertical = dimens.space4
                    ),
                    modifier = Modifier.height(dimens.space32)
                ) {
                    Text(
                        text = "+ ${stringResource(R.string.widget_add_to_home)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }

            // The Inner Widget Visual Preview
            content()
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Next Prayer Preview Card (Image 2 - Pixel-Perfect)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun NextPrayerWidgetPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF156456),
                        Color(0xFF0D443A),
                        Color(0xFF062620)
                    )
                )
            )
            .border(
                width = dimens.divider,
                color = Color(0x405EEAD4),
                shape = RoundedCornerShape(dimens.cornerLg)
            )
            .padding(dimens.space16)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PulsingLiveDot(
                    color = Color(0xFF5EEAD4)
                )
                Spacer(modifier = Modifier.width(dimens.space8))
                Text(
                    text = "NEXT PRAYER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5EEAD4)
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_sun_mint),
                    contentDescription = null,
                    tint = Color(0xFF5EEAD4),
                    modifier = Modifier.size(dimens.iconSm)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dhuhr",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(dimens.space2))
                    Text(
                        text = "12:34 PM",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF99E6DB)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "IN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF80CBC4)
                    )
                    Spacer(modifier = Modifier.height(dimens.space2))
                    Text(
                        text = "01:59:42",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.compButton)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF059669),
                                Color(0xFF10B981),
                                Color(0xFF059669)
                            )
                        ),
                        shape = RoundedCornerShape(dimens.cornerFull)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_prayer_log),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                    Text(
                        text = "Log Prayer",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hijri Date & Event Preview Card (Widget 2 - 1:1 Pixel Match)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HijriDateWidgetPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF251C0F),
                        Color(0xFF1B130A),
                        Color(0xFF110C05)
                    )
                )
            )
            .border(
                width = dimens.divider,
                color = Color(0xFF48361C),
                shape = RoundedCornerShape(dimens.cornerLg)
            )
            .padding(dimens.space16)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // 1. Top Row: Golden Badge on Left + Gregorian Date Chip on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Hijri Date Badge
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF302312),
                            shape = RoundedCornerShape(dimens.cornerSm)
                        )
                        .border(
                            width = dimens.divider,
                            color = Color(0xFF4D3A1F),
                            shape = RoundedCornerShape(dimens.cornerSm)
                        )
                        .padding(horizontal = dimens.space8, vertical = dimens.space4)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar_gold),
                            contentDescription = null,
                            tint = Color(0xFFFCD34D),
                            modifier = Modifier.size(dimens.iconXs)
                        )
                        Text(
                            text = "HIJRI DATE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFCD34D)
                            )
                        )
                    }
                }

                // Gregorian Date Chip
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF2C2011),
                            shape = RoundedCornerShape(dimens.cornerFull)
                        )
                        .border(
                            width = dimens.divider,
                            color = Color(0xFF48361C),
                            shape = RoundedCornerShape(dimens.cornerFull)
                        )
                        .padding(horizontal = dimens.space12, vertical = dimens.space4)
                ) {
                    Text(
                        text = "Wednesday, 13 May 2026",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFDE68A)
                        )
                    )
                }
            }

            // 2. Middle: Large Day Number + Month & Year
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = "15",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFEF3C7)
                    )
                )
                Text(
                    text = "Rabi' al-Awwal 1447 AH",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFBBF24)
                    )
                )
            }

            // 3. Subtle Golden Divider Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.divider)
                    .background(Color(0xFF382A14))
            )

            // 4. Bottom Row: Star Icon + Event on Left | View Calendar > on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sparkle_gold),
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(dimens.iconXs)
                    )
                    Text(
                        text = "12 Rabi' al-Awwal",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE6DFD5)
                        ),
                        maxLines = 1
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Text(
                        text = "View Calendar",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFBBF24)
                        )
                    )
                    Text(
                        text = "›",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFBBF24)
                        )
                    )
                }
            }
        }
    }
}

/**
 * 1:1 Pixel-Matched Preview of Widget 3: Hijri Full Calendar
 */
@Composable
fun HijriFullCalendarWidgetPreview() {
    val cardGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF111F27),
            Color(0xFF0C171D),
            Color(0xFF081015)
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = androidx.compose.foundation.BorderStroke(dimens.divider, Color(0xFF1D3340)),
        elevation = CardDefaults.cardElevation(dimens.elevation2)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(dimens.space16)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                // 1. Top Header Row: Icon + Title/Month + Full Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(dimens.space32)
                                .clip(RoundedCornerShape(dimens.cornerSm))
                                .background(Color(0xFF143749)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar_cyan),
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(dimens.iconXs)
                            )
                        }

                        Column {
                            Text(
                                text = "Hijri Calendar",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Rabi' al-Awwal 1447",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF7DD3FC)
                                )
                            )
                        }
                    }

                    Text(
                        text = "Full",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(dimens.space4))

                // 2. Weekday Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val headers = listOf("S", "M", "T", "W", "T", "F", "S")
                    headers.forEach { h ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = h,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )
                            )
                        }
                    }
                }

                // 3. Days Grid Preview (5 Weeks matching reference design)
                val sampleDays = listOf(
                    listOf(null, null, null, 1, 2, 3, 4),
                    listOf(5, 6, 7, 8, 9, 10, 11),
                    listOf(12, 13, 14, 15, 16, 17, 18),
                    listOf(19, 20, 21, 22, 23, 24, 25),
                    listOf(26, 27, 28, 29, 30, null, null)
                )

                sampleDays.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        week.forEach { d ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(dimens.space32),
                                contentAlignment = Alignment.Center
                            ) {
                                if (d != null) {
                                    when (d) {
                                        15 -> {
                                            // Today (Electric Cyan Glow)
                                            Box(
                                                modifier = Modifier
                                                    .size(dimens.space24)
                                                    .clip(RoundedCornerShape(dimens.cornerSm))
                                                    .background(Color(0xFF00A3FF)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "$d",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                )
                                            }
                                        }
                                        1, 27 -> {
                                            // Event Days (Dark Cyan Capsule)
                                            Box(
                                                modifier = Modifier
                                                    .size(dimens.space24)
                                                    .clip(RoundedCornerShape(dimens.cornerSm))
                                                    .background(Color(0xFF143749)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "$d",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF38BDF8)
                                                    )
                                                )
                                            }
                                        }
                                        else -> {
                                            Text(
                                                text = "$d",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFFCBD5E1)
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



