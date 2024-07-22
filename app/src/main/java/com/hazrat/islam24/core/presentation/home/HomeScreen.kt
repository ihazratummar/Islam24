package com.hazrat.islam24.core.presentation.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.presentation.common.LocationName
import com.hazrat.islam24.core.presentation.home.component.DisplayCurrentPrayerName
import com.hazrat.islam24.core.presentation.home.component.DisplayCurrentPrayerTime
import com.hazrat.islam24.core.presentation.home.component.LazyRowWithCards
import com.hazrat.islam24.core.presentation.home.component.shimmerEffect
import com.hazrat.islam24.presentation.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.DateUtil.getCurrentDay

@Composable
fun HomeScreen(
    navController: NavController,
    navigateToPrayerTime: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {

    val prayerTimes = viewModel.prayerTimes.collectAsState().value
    val locationName = viewModel.locationName.collectAsState().value

    LazyColumn(
        modifier = Modifier
    ) {
        item {
            Surface(
                modifier = Modifier
            ) {
                BackGroundCard()
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxSize()
                        .padding(MaterialTheme.dimens.size10),
                    verticalArrangement = Arrangement.Top
                ) {
//                    Text(text = "Salam", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size100))
                    if (prayerTimes.isNotEmpty() && locationName.isNotEmpty()) {
                        TimeLocationCard(prayerTimes, navigateToPrayerTime, locationName.first())
                    } else {
                        // Handle the case where prayerTimes is empty
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.size200)
                                .shimmerEffect()
                                .clickable {
                                    navigateToPrayerTime()
                                },
                            shape = RoundedCornerShape(MaterialTheme.dimens.size30),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                        ) {

                        }
                    }
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size10),
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
fun BackGroundCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            bottomEnd = MaterialTheme.dimens.size50,
            bottomStart = MaterialTheme.dimens.size50
        ),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.group),
            contentDescription = "masjidimage",
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = MaterialTheme.dimens.size50,
                        bottomEnd = MaterialTheme.dimens.size50
                    )
                )
                .size(MaterialTheme.dimens.size300),
            colorFilter = ColorFilter.colorMatrix(colorMatrix = ColorMatrix())
        )
    }
}


/// TIME LOCATION CARD
@Composable
private fun TimeLocationCard(
    prayerTimeEntity: List<PrayerTimeEntity>,
    navigateToPrayerTime: () -> Unit,
    locationDetailsEntity: LocationDetailsEntity,
    viewModel: MainViewModel = hiltViewModel(),
    homeViewModel: HomeScreenViewModel = hiltViewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.size200)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(0.6f),
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                ),
                shape = RoundedCornerShape(MaterialTheme.dimens.size30),
            )
            .clickable {
                navigateToPrayerTime()
            },
        colors = CardDefaults.cardColors(Color.Transparent),

        ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size10)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f)
                    .padding(
                        start = MaterialTheme.dimens.size20,
                        bottom = MaterialTheme.dimens.size15
                    ),
                verticalArrangement = Arrangement.Bottom
            ) {
                DisplayCurrentPrayerName(
                    prayerTimeEntity,
                )

                Text(
                    text = stringResource(R.string.view_salat_times),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f)
                    .padding(
                        start = MaterialTheme.dimens.size5,
                        bottom = MaterialTheme.dimens.size15,
                        end = MaterialTheme.dimens.size10,
                        top = MaterialTheme.dimens.size20
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {

                LocationName(locationDetailsEntity)
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))
                val grday = getCurrentDay()
                val hijriday = viewModel.getHijriDay()
                if (homeViewModel.isPrayerTime(prayerTimeEntity, grday, hijriday)) {
                    Text(
                        text = stringResource(id = R.string.now),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    DisplayCurrentPrayerTime(prayerTimeEntity, grday, hijriday)
                }
            }
        }
    }
}