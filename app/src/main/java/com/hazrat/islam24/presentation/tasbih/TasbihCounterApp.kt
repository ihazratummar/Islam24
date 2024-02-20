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
import com.hazrat.islam24.presentation.Dimens.Size1
import com.hazrat.islam24.presentation.Dimens.Size10
import com.hazrat.islam24.presentation.Dimens.Size15
import com.hazrat.islam24.presentation.Dimens.Size150
import com.hazrat.islam24.presentation.Dimens.Size2
import com.hazrat.islam24.presentation.Dimens.Size20
import com.hazrat.islam24.presentation.Dimens.Size200
import com.hazrat.islam24.presentation.Dimens.Size300
import com.hazrat.islam24.presentation.Dimens.Size4
import com.hazrat.islam24.presentation.Dimens.Size5
import com.hazrat.islam24.presentation.Dimens.Size50
import com.hazrat.islam24.presentation.Dimens.Size8
import com.hazrat.islam24.presentation.Dimens.Size9
import com.hazrat.islam24.presentation.Dimens.SpSize20
import com.hazrat.islam24.presentation.Dimens.SpSize30
import com.hazrat.islam24.presentation.Dimens.SpSize50
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

        Spacer(modifier = Modifier.height(Size10))
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
                        .width(Size150)
                        .height(Size50)
                        .padding(Size5),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(Size1, color = Color.White),
                ) {
                    Row(modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Set Count: $repeatCount", modifier = Modifier
                                .padding(Size5),
                            fontFamily = Hidayat
                        )
                    }
                }

                // Round Counter
                Card(
                    modifier = Modifier
                        .width(Size150)
                        .height(Size50)
                        .padding(Size5),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(Size1, color = Color.White)
                ) {
                    Row (modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            "Round: $roundCount",
                            modifier = Modifier
                                .padding(Size5),
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
                    .size(Size300)
                    .padding(Size15),
                shape = CircleShape,
                border = BorderStroke(Size2, Color.White),
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
                            fontSize = SpSize50,
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
        Spacer(modifier = Modifier.height(Size10))

        // Reset Button
        Button(
            onClick = {
                viewModel.resetTasbihCount()
                roundCount = 0
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Size10),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = BorderStroke(Size1, color = Color(0xFFFFFFFF)),
            shape = RoundedCornerShape(Size9)
        ) {
            Text("Reset",fontFamily = Hidayat, color = Color.White, fontSize = SpSize20)
        }
        Spacer(modifier = Modifier.height(Size10))

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
                .width(Size200)
                .height(Size50)
                .padding(Size5),
            colors = CardDefaults.cardColors(Color.Transparent),
            border = BorderStroke(Size1, color = Color.White)
        ) {
            Row(modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Total Count: ${tasbih?.totalCount}",
                    color = Color.White,
                    modifier = Modifier
                        .padding(Size5),
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
            .padding(Size8)
            .width(Size300)
            .height(Size200)
            .clickable {
                viewModel.selectedPhrase = phrase
                viewModel.resetTasbihCount()
            },
        border = BorderStroke(Size1, color = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(Size4),
        colors = CardDefaults.cardColors(
            containerColor = if (phrase == selectedPhrase) Color(0xFF195710) else Color(0xFF042C15)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Size20, vertical = Size50),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                phrase.arText, style =
                TextStyle(fontWeight = FontWeight.Bold),
                color = Color.Green,
                fontSize = SpSize30,
                textAlign = TextAlign.Center
            )
            Text(
                phrase.enText, style =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontFamily = Hidayat
                ),
                fontSize = SpSize20,
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