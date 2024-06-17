package com.hazrat.islam24.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.R
import com.hazrat.islam24.ui.theme.dimens

@Composable
fun LazyRowWithCards(navController: NavController) {

    val icons = listOf(
        R.drawable.allahname,
        R.drawable.tasbihicon,
//        R.drawable.duaicon,
        R.drawable.calendaricon,
        R.drawable.athkar,)

    val names = listOf(
        "Names", "Tasbih", "Calendar", "Athkar"
    )

    LazyRow{
        items(icons.size){index ->
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                val cardModifier = when (index) {
                    0 -> Modifier.background(Color(0xFFE6C556))
                    1 -> Modifier.background(Color(0xFF7AFFFF))
//                    2 -> Modifier.background(Color(0xFFFFBFA6))
                    2 -> Modifier.background(Color(0xFFFF958C))
                    3 -> Modifier.background(Color(0xffFBBC05))
                    else -> Modifier.background(Color.Gray)
                }
                Card(
                    modifier = Modifier
                        .width(MaterialTheme.dimens.size80)
                        .height(MaterialTheme.dimens.size80)
                        .padding(MaterialTheme.dimens.size8)
                        .clickable {
                            when (index) {
                                0 -> navController.navigate("NamesOfAllah")
                                1 -> navController.navigate("TasbihScreen")
//                                2 -> navController.navigate("DuasPageScreen")
                                2 -> navController.navigate("CalendarScreen")
                                3 -> navController.navigate("AthkarScreen")
                            }
                        }
                        .clip(RoundedCornerShape(MaterialTheme.dimens.size8))
                        .then(cardModifier),
                    colors = CardDefaults.cardColors(Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(MaterialTheme.dimens.size100)
                    )
                }
                Text(names[index], color = colorResource(id = R.color.text))
            }
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size10))

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