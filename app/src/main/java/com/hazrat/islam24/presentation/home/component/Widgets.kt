package com.hazrat.islam24.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.R
import com.hazrat.islam24.presentation.Dimens.Size8
import com.hazrat.islam24.presentation.Dimens.Size80
import com.hazrat.islam24.presentation.Dimens.Size90

@Composable
fun LazyRowWithCards(navController: NavController) {

    val icons = listOf(
        R.drawable.allahname,
        R.drawable.tasbihicon,
        R.drawable.duaicon,
        R.drawable.zakaticon,
        R.drawable.calendaricon,
        R.drawable.hadithicon,
        R.drawable.athkar,
        R.drawable.quiz,)

    val names = listOf(
        "Names", "Tasbih", "Dua", "Zakat", "Calendar", "Hadith", "Athkar", "Quiz"
    )

    LazyRow{
        items(icons.size){index ->
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                val cardModifier = when (index) {
                    0 -> Modifier.background(Color(0xFFE6C556))
                    1 -> Modifier.background(Color(0xFF6AAEE9))
                    2 -> Modifier.background(Color(0xFF64FFAA))
                    3 -> Modifier.background(Color(0xffFBBC05))
                    4 -> Modifier.background(Color(0xffEA4335))
                    5 -> Modifier.background(Color(0xFF144BA7))
                    6 -> Modifier.background(Color(0xffFBBC05))
                    7 -> Modifier.background(Color(0xff34A853))
                    8 -> Modifier.background(Color(0xffEA4335))
                    else -> Modifier.background(Color.Gray)
                }
                Card(
                    modifier = Modifier
                        .width(Size80)
                        .height(Size80)
                        .padding(Size8)
                        .clickable {
                            when(index){
                                0 -> navController.navigate("homeScreen")
                                1 -> navController.navigate("PrayerTimeScreen")
                            }
                        }
                        .clip(RoundedCornerShape(Size8))
                        .then(cardModifier),
                    colors = CardDefaults.cardColors(Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(Size80)
                    )
                }
                Text(names[index], color = Color.White)
            }

        }
    }
}



@Composable
fun MyScreen(navController: NavController) {
    Surface(){
        LazyRowWithCards(navController)
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
fun MyScreenPreview() {
    val navController = rememberNavController()
    MyScreen(navController = navController)
}