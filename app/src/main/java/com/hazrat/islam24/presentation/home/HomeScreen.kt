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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.hazrat.islam24.R
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
import com.hazrat.islam24.presentation.home.component.LazyRowWithCards
import com.hazrat.islam24.ui.theme.Islam24Theme

@Composable
fun HomeScreen(navController: NavController) {

    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        BackGroundCard()
        Column(
            modifier = Modifier.statusBarsPadding()
                .fillMaxSize()
                .padding(Size20),
            verticalArrangement = Arrangement.Top
        ) {

            Text(text = "Assalamualaikum", style = MaterialTheme.typography.bodySmall)
//            Text(text = "Hazrat Ummar Shaikh", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(Size50))
            TimeLocationCard()

            Spacer(modifier = Modifier.height(Size30))

            Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){

                LazyRowWithCards(navController)
            }

        }
    }
}




////BACKGROUND CARD WITH MASJID ICON
@Composable
private fun BackGroundCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Size250),
        shape = RoundedCornerShape(bottomEnd = Size50, bottomStart = Size50),
        colors = CardDefaults.cardColors(Color(0xFF7FD158))
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
private fun TimeLocationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Size200)
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xCE031B05),
//                        Color.Transparent,
                        Color(0xF210F742),
                    )
                ),
                shape = RoundedCornerShape(28)
            )
            .clickable {
                       /* TODO click to open prayer screen*/
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
                Text(
                    text = "Maghrib",
                    color = Color.White,
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.height(Size8))
                Text(text = "View Times", color = Color.White, style = MaterialTheme.typography.bodyMedium)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f)
                    .padding(start = Size10, bottom = Size40, end = Size30),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "Ajagarpara", fontWeight = FontWeight.SemiBold, color = Color.White)
                Text(text = "-0:55:18", fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview(navController: NavController = NavController(context = LocalContext.current)) {
    Islam24Theme {
        HomeScreen(navController = navController)
    }
}

