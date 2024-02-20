package com.hazrat.islam24.presentation.tasbih

import android.content.Context
import android.os.Vibrator
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.islam24.data.dao.TasbihCounterDao
import com.hazrat.islam24.data.entity.TasbihCounterEntity
import com.hazrat.islam24.data.manager.TasbihRepositoryImpl
import com.hazrat.islam24.domain.model.TasbihPhrase
import com.hazrat.islam24.domain.model.tasbihPhraseList
import com.hazrat.islam24.domain.repository.TasbihRepository
import com.hazrat.islam24.presentation.tasbih.tasbihcomponent.RepeatCountDialog
import com.hazrat.islam24.ui.theme.Hidayat
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.util.vibrate

@Composable
fun TasbihCounterApp(viewModel: TasbihViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val myTasbihCounter by viewModel.tasbihCounter.collectAsState(initial = emptyList())
    val tasbih = myTasbihCounter.firstOrNull()


//    val selectedPhrase by remember { mutableStateOf(tasbihPhraseList[0]) }
    var repeatCount by remember { mutableIntStateOf(33) } // Start from 33 initially
    var tasbihCount by remember { mutableIntStateOf(tasbih?.tasbihCount?:1) }
    var totalCount by remember { mutableIntStateOf(tasbih?.totalCount?:1) }
    var roundCount by remember { mutableIntStateOf(0) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Phrase Selection
        LazyRow {
            items(tasbihPhraseList) { phrase ->
                TasbihHeader(phrase, viewModel)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Column (modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            ToTalCount(tasbih)
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Repeat Count Selection
                Card(
                    modifier = Modifier
                        .clickable { isDialogOpen = true }
                        .width(150.dp)
                        .height(50.dp)
                        .padding(5.dp),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(1.dp, color = Color.White),
                ) {
                    Row(modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Set Count: $repeatCount", modifier = Modifier
                                .padding(5.dp),
                            fontFamily = Hidayat
                        )
                    }
                }

                // Round Counter
                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .padding(5.dp),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(1.dp, color = Color.White)
                ) {
                    Row (modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            "Round: $roundCount",
                            modifier = Modifier
                                .padding(5.dp),
                            fontFamily = Hidayat
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tasbih Count Display
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {

            Button(
                onClick = {

                    if (tasbih != null) {
                        viewModel.insertTasbih(
                            TasbihCounterEntity(
                                totalCount = tasbih.totalCount + 1,
                                tasbihCount = tasbih.tasbihCount + 1
                            )
                        )
                    }else{
                        viewModel.insertTasbih(
                            TasbihCounterEntity(
                                totalCount = totalCount,
                                tasbihCount = tasbihCount
                            )
                        )
                    }

                    totalCount++
                    tasbihCount++
                    if (tasbihCount % repeatCount == 0) {
                        roundCount++
                        viewModel.resetTasbihCount()
                    }
                    vibrate(vibrator!!)
                },
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp),
                shape = CircleShape,
                border = BorderStroke(2.dp, Color.White),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${tasbih?.tasbihCount} / $repeatCount",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 50.sp,
                            color = Color.White,
                            fontFamily = Hidayat
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (tasbih?.tasbihCount == 0) Text(
                        "Click to Start", color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

            }
        }


        // Big Circle Counter
        Spacer(modifier = Modifier.height(10.dp))

        // Reset Button
        Button(
            onClick = {
                viewModel.resetTasbihCount()
                roundCount = 0
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, color = Color(0xFFFFFFFF)),
            shape = RoundedCornerShape(9.dp)
        ) {
            Text("Reset",fontFamily = Hidayat, color = Color.White, fontSize = 20.sp)
        }

        // Dialog for Repeat Count Selection
        if (isDialogOpen) {
            RepeatCountDialog(
                onDismiss = { isDialogOpen = false },
                onRepeatCountSelected = { selectedCount ->
                    repeatCount = selectedCount
                    isDialogOpen = false
                }
            )
        }
    }

}

@Composable
private fun ToTalCount(tasbih: TasbihCounterEntity?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
                .padding(5.dp),
            colors = CardDefaults.cardColors(Color.Transparent),
            border = BorderStroke(1.dp, color = Color.White)
        ) {
            Row(modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Total Count: ${tasbih?.totalCount}",
                    color = Color.White,
                    modifier = Modifier
                        .padding(5.dp),
                    fontFamily = Hidayat
                )
            }
        }
    }
}

@Composable
private fun TasbihHeader(
    phrase: TasbihPhrase,
    viewModel: TasbihViewModel
) {
    val selectedPhrase = viewModel.selectedPhrase
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(300.dp)
            .height(200.dp)
            .clickable {
                viewModel.selectedPhrase = phrase
                viewModel.resetTasbihCount()
            },
        border = BorderStroke(1.dp, color = Color(0xFFFFBF00)),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (phrase == selectedPhrase) Color(0xFF195710) else Color(0xFF042C15)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                phrase.arText, style =
                TextStyle(fontWeight = FontWeight.Bold),
                color = Color.Green,
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
            Text(
                phrase.enText, style =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontFamily = Hidayat
                ),
                fontSize = 20.sp,
                textAlign = TextAlign.Center

            )
        }
    }
}



@Preview()
@Composable
fun PreviewTasbihCounterApp() {
    Islam24Theme {
        val tasbihCounterEntity = TasbihCounterEntity(totalCount = 10)
        ToTalCount(tasbihCounterEntity)
    }
}