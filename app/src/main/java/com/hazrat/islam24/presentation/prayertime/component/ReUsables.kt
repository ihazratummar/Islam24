package com.hazrat.islam24.presentation.prayertime.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazrat.islam24.R
import com.hazrat.islam24.presentation.Dimens.Size10
import com.hazrat.islam24.presentation.Dimens.Size20
import com.hazrat.islam24.presentation.Dimens.Size30
import com.hazrat.islam24.presentation.Dimens.Size8
import com.hazrat.islam24.presentation.Dimens.SpSize17
import com.hazrat.islam24.presentation.Dimens.SpSize20
import com.hazrat.islam24.ui.theme.Islam24Theme


@Composable
fun PrayerTimeSettingCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    subText: String?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Size8)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Size8)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                modifier = Modifier.size(Size30)
            )
            Column(
                modifier = Modifier.padding(horizontal = Size10)
            ) {
                Spacer(modifier = Modifier.width(Size8))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (subText != null) {
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}


@Composable
fun PrayerTimeCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    time: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Size20),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.2f)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "Icon",
                    tint = Color.Green.copy(0.5f),
                    modifier = Modifier.size(Size30)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = text,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = SpSize20,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(start = Size10)
                )
            }
            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                Text(
                    text = time,
                    style = TextStyle(
                        color = Color.Green,
                        fontSize = SpSize20,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(start = Size10)
                )
            }
        }
    }
}


@Composable
fun PrayerDateCard(
    modifier: Modifier = Modifier,
    enDate: String,
    hrDate: String
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color =  Color.Transparent,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = enDate,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = SpSize20,
                        fontWeight = FontWeight.SemiBold))
                Text(text = hrDate,
                    style = TextStyle(
                        color = Color.Green,
                        fontSize = SpSize17))
            }
        }
    }
}

@Preview
@Composable
fun PrayerDateCardPreview() {
    Islam24Theme {
        PrayerDateCard(enDate = "Monday, 12 February", hrDate = "2 Sha'ban 1445")
    }
}

@Preview
@Composable
fun PrayerTimeCardPreview() {
    Islam24Theme {
        PrayerTimeCard(icon = R.drawable.athkar,
            text = "Fajr",
            time = "04:59",
            onClick = {}
        )
    }

}


@Preview
@Composable
fun ClickableCardPreview() {
    Islam24Theme {
        PrayerTimeSettingCard(
            icon = R.drawable.athkar,
            text = "Go Back",
            subText = "Prayermethod",
            onClick = {
                // Handle onClick action here
            }
        )
    }
}