package com.hazrat.islam24.presentation.navigator.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.presentation.home.BackGroundCard
import com.hazrat.islam24.presentation.home.component.LazyRowWithCards
import com.hazrat.islam24.presentation.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens

@Composable
fun NoInternet(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
        ) {

            item {
                Surface(
                    modifier = Modifier.padding(MaterialTheme.dimens.size5),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BackGroundCard()
                    Column(
                        modifier = Modifier
                            .statusBarsPadding()
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.size10),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(text = "Salam", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size60))
                        // Handle the case where prayerTimes is empty
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.size200)
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color(0xC31F581F),
                                            Color(0xFF054105),
                                            Color(0xFF45D307),
                                        )
                                    ),
                                    shape = RoundedCornerShape(MaterialTheme.dimens.size30)
                                ),

                            colors = CardDefaults.cardColors(Color.Transparent),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Salat Time Loading ",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Spacer(modifier = Modifier.size(MaterialTheme.dimens.size100))
                        CircularProgressIndicator(
                            modifier = Modifier.size(MaterialTheme.dimens.size60)
                        )
                        Spacer(modifier = Modifier.size(MaterialTheme.dimens.size50))
                        val network = viewModel.networkStatus.value
                        Text(text = "Network status: $network", color = Color.White)
                    }
                }
            }
        }
    }
}
