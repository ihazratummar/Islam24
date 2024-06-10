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
import com.hazrat.islam24.data.entity.TasbihCounterEntity
import com.hazrat.islam24.domain.model.TasbihPhrase
import com.hazrat.islam24.domain.model.tasbihPhraseList
import com.hazrat.islam24.presentation.tasbih.tasbihcomponent.RepeatCountDialog
import com.hazrat.islam24.ui.theme.Hidayat
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.vibrate

@Composable
fun TasbihCounterApp(viewModel: TasbihViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val myTasbihCounter by viewModel.tasbihCounter.collectAsState(initial = emptyList())
    val tasbih = myTasbihCounter.firstOrNull()


//    val selectedPhrase by remember { mutableStateOf(tasbihPhraseList[0]) }
    var repeatCount by remember { mutableIntStateOf(33) } // Start from 33 initially
    var tasbihCount by remember { mutableIntStateOf(tasbih?.tasbihCount ?: 1) }
    var totalCount by remember { mutableIntStateOf(tasbih?.totalCount ?: 1) }
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

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size10))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ToTalCount(tasbih)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Repeat Count Selection
                Card(
                    modifier = Modifier
                        .clickable { isDialogOpen = true }
                        .width(MaterialTheme.dimens.size150)
                        .height(MaterialTheme.dimens.size50)
                        .padding(MaterialTheme.dimens.size5),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(MaterialTheme.dimens.size1, color = MaterialTheme.colorScheme.primary),
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Count: $repeatCount", modifier = Modifier,
                            color = Color.White
                        )
                    }
                }

                // Round Counter
                Card(
                    modifier = Modifier
                        .width(MaterialTheme.dimens.size150)
                        .height(MaterialTheme.dimens.size50)
                        .padding(MaterialTheme.dimens.size5),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(MaterialTheme.dimens.size1, color = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Round: $roundCount",
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.size5),
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size10))

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
                    } else {
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
                    .size(MaterialTheme.dimens.size300)
                    .padding(MaterialTheme.dimens.size15),
                shape = CircleShape,
                border = BorderStroke(MaterialTheme.dimens.size2, Color.White),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val countText = "${tasbih?.tasbihCount ?: "0"} / $repeatCount"
                    Text(
                        text = countText,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (tasbih?.tasbihCount == 0) Text(
                        "Click to Start", color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

            }
        }


        // Big Circle Counter
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size10))

        // Reset Button
        Button(
            onClick = {
                viewModel.resetTasbihCount()
                roundCount = 0
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.size10),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = BorderStroke(MaterialTheme.dimens.size1, color =MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(MaterialTheme.dimens.size9)
        ) {
            Text(
                "Reset",color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displaySmall
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size10))

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
                .width(MaterialTheme.dimens.size200)
                .height(MaterialTheme.dimens.size50)
                .padding(MaterialTheme.dimens.size5),
            colors = CardDefaults.cardColors(Color.Transparent),
            border = BorderStroke(MaterialTheme.dimens.size1, color = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val countText = tasbih?.totalCount ?: "0"
                Text(
                    "Total Count: $countText",
                    color = Color.White,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.size5)
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
            .padding(MaterialTheme.dimens.size8)
            .width(MaterialTheme.dimens.size300)
            .height(MaterialTheme.dimens.size200)
            .clickable {
                viewModel.selectedPhrase = phrase
                viewModel.resetTasbihCount()
            },
        elevation = CardDefaults.cardElevation(MaterialTheme.dimens.size4),
        colors = CardDefaults.cardColors(
            containerColor = if (phrase == selectedPhrase) MaterialTheme.colorScheme.primary.copy(0.6f)
            else MaterialTheme.colorScheme.primary.copy(0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.dimens.size10),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                phrase.arText, style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
            )
            Text(
                phrase.enText, style = MaterialTheme.typography.headlineMedium,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = Color.White

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