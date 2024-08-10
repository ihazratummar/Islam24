package com.hazrat.islam24.core.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.core.presentation.home.component.BackGroundCard
import com.hazrat.islam24.core.presentation.home.component.LazyVerticalGridCardIcons
import com.hazrat.islam24.core.presentation.home.component.TimeLocationCard
import com.hazrat.islam24.core.presentation.home.component.shimmerEffect
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens

@Composable
fun HomeScreen(
    navController: NavController,
    navigateToPrayerTime: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    val locationName by viewModel.locationName.collectAsState()
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
                        .padding(dimens.size10),
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(dimens.size100))
                    if (prayerTimes.isNotEmpty() && locationName.isNotEmpty()) {
                        TimeLocationCard(prayerTimes, navigateToPrayerTime, locationName.first())
                    } else {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimens.size200)
                                .shimmerEffect(),
                            shape = RoundedCornerShape(dimens.size30),
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
                    .padding(dimens.size10),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGridCardIcons(navController)
            }
        }
    }
}