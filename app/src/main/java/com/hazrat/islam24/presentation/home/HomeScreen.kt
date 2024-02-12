package com.hazrat.islam24.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hazrat.islam24.R
import com.hazrat.islam24.data.prayertime.PrayerTimeEntity
import com.hazrat.islam24.presentation.Dimens.Size10
import com.hazrat.islam24.presentation.Dimens.Size20
import com.hazrat.islam24.presentation.Dimens.Size200
import com.hazrat.islam24.presentation.Dimens.Size250
import com.hazrat.islam24.presentation.Dimens.Size30
import com.hazrat.islam24.presentation.Dimens.Size300
import com.hazrat.islam24.presentation.Dimens.Size40
import com.hazrat.islam24.presentation.Dimens.Size50
import com.hazrat.islam24.presentation.Dimens.Size60
import com.hazrat.islam24.presentation.Dimens.Size8
import com.hazrat.islam24.presentation.Dimens.SpSize20
import com.hazrat.islam24.presentation.home.component.LazyRowWithCards
import com.hazrat.islam24.presentation.nvgraph.Route
import com.hazrat.islam24.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.presentation.prayertime.component.DisplayCurrentPrayerName
import com.hazrat.islam24.presentation.prayertime.component.DisplayCurrentPrayerTime
import com.hazrat.islam24.ui.theme.Islam24Theme

@Composable
fun HomeScreen(
    navController: NavController,
    prayerTimeViewModel: PrayerTimeViewModel,
    navigateToPrayerTime: () -> Unit,
) {

    val prayerTimesState = prayerTimeViewModel.prayerTimes.collectAsState()
    val prayerTimes = prayerTimesState.value

    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        BackGroundCard()
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(Size10),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "", style = MaterialTheme.typography.bodySmall)
//            Text(text = "Hazrat Ummar Shaikh", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(Size60))
            if (prayerTimes.isNotEmpty()) {
                TimeLocationCard(prayerTimes.first(), navigateToPrayerTime)
            } else {
                // Handle the case where prayerTimes is empty
                Text(text = "No prayer times available")
            }

            Spacer(modifier = Modifier.height(Size30))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                LazyRowWithCards(navController)
            }

        }
    }
}


////BACKGROUND CARD WITH MASJID ICON
//@Preview
@Composable
private fun BackGroundCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Size250)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF041602),
                        Color(0xFF031600)
                    ),

                    )
            ),
        shape = RoundedCornerShape(bottomEnd = Size50, bottomStart = Size50),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.group),
            contentDescription = "masjidimage",
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(bottomStart = Size50, bottomEnd = Size50))
                .size(Size300)

        )
    }
}


/// TIME LOCATION CARD
@Composable
private fun TimeLocationCard(
    prayerTimeEntity: PrayerTimeEntity,
    navigateToPrayerTime: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Size200)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xC3040A04),
                        Color(0xFF040A04),
                        Color(0xFF00FF40),
                    )
                ),
                shape = RoundedCornerShape(28)
            )
            .clickable {
                navigateToPrayerTime()
            },
        colors = CardDefaults.cardColors(Color.Transparent),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Size10)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f)
                    .padding(start = Size10, bottom = Size30),
                verticalArrangement = Arrangement.Bottom
            ) {
                DisplayCurrentPrayerName(
                    prayerTimeEntity, textStyle = TextStyle(
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(Size8))
                Text(
                    text = "View Times",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f)
                    .padding(start = Size10, bottom = Size40, end = Size30, top = Size10),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "Location", fontWeight = FontWeight.SemiBold, color = Color.White)
                DisplayCurrentPrayerTime(
                    prayerTimeEntity,
                    textStyle = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = SpSize20,
                        color = Color.White
                    )
                )
            }
        }
    }
}
