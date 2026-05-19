package com.hazrat.prayer.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Space
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hazrat.ui.R
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.common.PrayerType
import com.hazrat.ui.theme.ActiveIcon
import com.hazrat.ui.theme.FajrGradient
import com.hazrat.ui.theme.InactiveIcon
import com.hazrat.ui.theme.Info
import com.hazrat.ui.theme.Islam24Theme
import com.hazrat.ui.theme.Success
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil
import com.hazrat.utils.DateUtil.toReadableDate


@Composable
fun PrayerProgressCard(
    modifier: Modifier = Modifier,
    todayTimeStamp: Long,
    prayerCompletePercent: Int = 0,
    completionRatio: Float = 0f,
    completePrayerCount: Int = 0

) {

    val animatedProgress by animateFloatAsState(
        targetValue = completionRatio,
        animationSpec = tween (
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnimation"
    )
    val animatedPercentage by animateIntAsState(
        targetValue = prayerCompletePercent,
        animationSpec = tween (
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "completePercentage"
    )


    Card(
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space8)
            ) {
                IconWithBackground(
                    icon = R.drawable.circle_check,
                    containerColor = customColors.accentColor.copy(0.1f),
                    iconColor = customColors.accentColor
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(dimens.space4),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "${todayTimeStamp.toReadableDate()}'s Progress",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "$completePrayerCount of 5 prayer logged",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = customColors.secondaryText,
                        )
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${animatedPercentage}%",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = customColors.accentColor,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space12)
                    .clip(shape = RoundedCornerShape(100))
                    .background(color = customColors.progressbarMute)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(customColors.accentColor)

                )
            }
        }
    }
}


@Composable
fun PrayerTimeCard(
    prayerType: PrayerType = PrayerType.ISHA,
    onNotificationClick: (PrayerType) -> Unit = {},
    onLogPrayerClick: (PrayerType) -> Unit = {},
    prayerTime: Long = 0L,
    isLogged: Boolean = false,
    isNotificationEnabled: Boolean= false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space4)
        ) {
            PrayerIconWithBackground(
                icon = prayerType.icon,
                containerColor = prayerType.gradient
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(dimens.space4),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(prayerType.nameRes),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                if (isLogged && prayerType != PrayerType.SUNRISE){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimens.space4),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.check),
                            contentDescription = null,
                            modifier = Modifier
                                .size(dimens.space8),
                            tint = Success
                        )
                        Text(
                            text = "Logged",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Success
                            )
                        )
                    }
                }

            }
            Spacer(Modifier.weight(1f))
            Text(
                text = DateUtil.dateLongToString(prayerTime),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            )
            if (prayerType != PrayerType.SUNRISE) {
                val notificationColor = if (isNotificationEnabled) Info else InactiveIcon
                IconWithBackground(
                    modifier = Modifier.size(dimens.space48 / 1.1f),
                    icon = if (isNotificationEnabled) R.drawable.notifications_fill else R.drawable.notificationoff,
                    iconColor = notificationColor,
                    containerColor = notificationColor.copy(0.1f),
                    onClick = {
                        onNotificationClick(prayerType)
                    }
                )
                val color = if (isLogged) Color.White else InactiveIcon
                val backgroundColor = if (isLogged) Success else InactiveIcon.copy(0.1f)

                IconWithBackground(
                    icon = R.drawable.check,
                    iconColor = color,
                    containerColor = backgroundColor,
                    onClick = {
                        onLogPrayerClick(prayerType)
                    }
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.space4)
                .clip(RoundedCornerShape(dimens.cornerLg))
                .background(
                    color = if (isLogged && prayerType != PrayerType.SUNRISE) Success else
                        MaterialTheme.colorScheme.outline
                )
        )
    }
}

data class PrayerTimeData(
    val prayerType: PrayerType,
    val prayerTime: Long,
)


@Composable
fun PrayerIconWithBackground(
    icon: Int,
    containerColor: List<Color> = FajrGradient,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(dimens.space48 * 1.05f)
            .padding(dimens.space4)
            .clip(RoundedCornerShape(dimens.space16))
            .background(
                brush = Brush.linearGradient(
                    colors = containerColor
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .padding(dimens.space12)
                .size(dimens.iconSm),
            tint = Color.White
        )
    }
}


@Composable
fun NotificationSettingCard(
    modifier: Modifier = Modifier,
    totalNotificationOn: Int = 0
) {
    Card(
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space8)
        ) {
            IconWithBackground(
                icon = R.drawable.notificationonn,
                containerColor = MaterialTheme.colorScheme.surfaceTint.copy(0.1f),
                iconColor = MaterialTheme.colorScheme.surfaceTint
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(dimens.space4),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Notification Settings",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "$totalNotificationOn of 5 prayer notified",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = customColors.secondaryText,
                    )
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.arrowright),
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSm),
                tint = customColors.secondaryText
            )
        }
    }
}

@Composable
fun PrayerSettingCard(
    text: String,
    methodID: String?,
    method: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.space12, vertical = dimens.space4)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer

        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = dimens.space20, vertical = dimens.space12),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.width(dimens.space8))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )
            if (methodID != null) {
                Text(
                    text = methodID,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            if (method != null) {
                Text(
                    text = method,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }

    }
}


@Composable
fun ToggleNotification(
    modifier: Modifier,
    isCheck: Boolean,
    notificationEvent: () -> Unit,
    notificationName: Int
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.space12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(notificationName))

                Switch(
                    checked = isCheck,
                    onCheckedChange = { notificationEvent.invoke() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedBorderColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        }
    }
}


@Composable
fun AzanList(
    modifier: Modifier = Modifier,
    onAzanPlayClick: (Int, String, String) -> Unit,
    isAzanPlaying: List<Boolean>,
    onAzanClick: (String, String) -> Unit,
    listOfAzan: List<AzanData>,
    onDefaultNotificationClick: () -> Unit,
    onSilentNotificationClick: () -> Unit,
    selectedOption: Int = 0,
    onOptionSelected: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onSilentNotificationClick()
                    onOptionSelected(0)
                },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedOption == 0) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.space32, horizontal = dimens.space12)
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.iconLg),
                        painter = painterResource(R.drawable.bell_off),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(dimens.space20))
                    Text("Silent")
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onDefaultNotificationClick()
                    onOptionSelected(1)
                },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedOption == 1) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.space32, horizontal = dimens.space12)
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.iconLg),
                        painter = painterResource(R.drawable.bell_ringing),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(dimens.space20))
                    Text("Default Notification")
                }
            }
        }
        itemsIndexed(listOfAzan) { index, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onAzanClick(item.url, item.name)
                    onOptionSelected(index + 2)
                },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedOption == index + 2) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.space32, horizontal = dimens.space12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.iconXl),
                        painter = painterResource(R.drawable.volume),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(dimens.space20))
                    Text(text = "Azan ${index + 1}")
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .size(dimens.iconLg)
                            .clickable {
                                if (index < isAzanPlaying.size) {
                                    onAzanPlayClick(index, item.name, item.url)
                                }
                            },
                        painter = if (index < isAzanPlaying.size && !isAzanPlaying[index]) {
                            painterResource(R.drawable.play)
                        } else {
                            painterResource(R.drawable.stop)
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(dimens.space4))

                }
            }
        }

    }
}

val listOfAzan = listOf(
    AzanData(
        name = "azan1",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan1.mp3"
    ),
    AzanData(
        name = "azan2",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan2.mp3"
    ),
    AzanData(
        name = "azan3",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan3.mp3"
    ),
    AzanData(
        name = "azan4",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan4.mp3"
    ),
    AzanData(
        name = "azan5",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan5.mp3"
    ),
    AzanData(
        name = "azan6",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan6.mp3"
    ),
    AzanData(
        name = "azan7",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan7.mp3"
    ),
    AzanData(
        name = "azan8",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan8.mp3"
    ),
    AzanData(
        name = "azan9",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan9.mp3"
    )
)

val listOfFajrAzan = listOf(
    AzanData(
        name = "fajr1",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/fajr1.mp3"
    ),
    AzanData(
        name = "fajr2",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/fajr2.mp3"
    )
)


data class AzanData(
    val name: String,
    val url: String
)
