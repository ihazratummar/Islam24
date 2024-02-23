package com.hazrat.islam24.presentation.prayertime.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.unit.sp
import com.hazrat.islam24.R
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.ui.theme.dimens


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
            .padding(MaterialTheme.dimens.size8)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = Color.Green.copy(0.1f),
        border = BorderStroke(MaterialTheme.dimens.size1, color = Color.Green)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(MaterialTheme.dimens.size8)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                modifier = Modifier.size(MaterialTheme.dimens.size30),
                tint = Color.White
            )
            Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size10)
            ) {
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))
                Text(
                    text = text,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                if (subText != null) {
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.labelMedium,
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
                .padding(MaterialTheme.dimens.size20),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.2f)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "Icon",
                    tint = MaterialTheme.colorScheme.primary.copy(0.8f),
                    modifier = Modifier.size(MaterialTheme.dimens.size30)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.displaySmall,color = Color.White,
                    modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
                )
            }
            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.displaySmall,color = Color.White,
                    modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size3))
                Text(text = hrDate,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 17.sp)
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun PrayerDateCardPreview() {
//    Islam24Theme {
//        PrayerDateCard(enDate = "Monday, 12 February", hrDate = "2 Sha'ban 1445")
//    }
//}

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


//@Preview
//@Composable
//fun ClickableCardPreview() {
//    Islam24Theme {
//        PrayerTimeSettingCard(
//            icon = R.drawable.athkar,
//            text = "Go Back",
//            subText = "Prayermethod",
//            onClick = {
//                // Handle onClick action here
//            }
//        )
//    }
//}